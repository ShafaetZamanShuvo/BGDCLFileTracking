<script setup>
import { ref, computed, onMounted } from 'vue';
import DefaultLayout from '../layouts/DefaultLayout.vue';
import FileService from '../services/FileService';
import { getUserInfo } from '../services/LoginService';
import Notification from './Notification.vue';

const success = ref('');
const error = ref('');
const requestedFiles = ref([]);
const currentUser = ref(null);
const isLoading = ref(true);
const notes = ref({});
const searchQuery = ref('');

const transferFileData = ref({
  "fileCode": "",
  "fromDepartmentId": '',
  "toDepartmentId": '',
  "fromUserId": '',
  "toUserId": '',
  "remarks": ""
});

const showSuccess = (message) => {
  error.value = '';
  success.value = message;
  setTimeout(() => {
    success.value = '';
  }, 5000);
};

// Error message handler
const showError = (message) => {
  success.value = '';
  error.value = message;
  setTimeout(() => {
    error.value = '';
  }, 5000);
};

onMounted(async () => {
    try {
        currentUser.value = await getUserInfo(localStorage.getItem('authToken'));
        if (currentUser.value && currentUser.value.departmentId) {
            requestedFiles.value = await FileService.getRequestedFilesListDetails(currentUser.value.departmentId);
            // Initialize notes object with file codes as keys
            requestedFiles.value.forEach(file => {
                notes.value[file.fileCode] = '';
            });
        } else {
            showError('Unable to load user information. Please log in again.');
        }
    } catch (err) {
        console.error('Error loading requested files:', err);
        showError('Error loading requested files. Please try again.');
    } finally {
        isLoading.value = false;
    }
});

// Filter files based on search query
const filteredFiles = computed(() => {
    if (!searchQuery.value.trim()) {
        return requestedFiles.value;
    }
    
    const query = searchQuery.value.toLowerCase();
    return requestedFiles.value.filter(file => 
        file.fileCode.toLowerCase().includes(query)
    );
});

const handleFileAction = async (file, accept) => {
    try {
        transferFileData.value = {
            "fileCode": file.fileCode,
            "fromDepartmentId": file.requestedFromDepartmentId,
            "toDepartmentId": file.requestingDepartmentId,
            "fromUserId": currentUser.value.userId,
            "toUserId":  file.requestingUserId,
            "remarks": notes.value[file.fileCode] || ''
        };
        

        await FileService.transferFile(transferFileData.value);
        
        // Remove the processed file from the list
        requestedFiles.value = requestedFiles.value.filter(f => f.fileCode !== file.fileCode || 
            f.requestingDepartmentId !== file.requestingDepartmentId);

        showSuccess(`File ${file.fileCode} has been ${accept ? 'accepted' : 'rejected'}`);
        
        // Optional: reload page after success, but with a timer for better UX
        setTimeout(() => {
            window.location.reload();
        }, 3000);
        
    } catch (err) {
        console.error(`Error ${accept ? 'accepting' : 'rejecting'} file:`, err);
        showError(`Failed to ${accept ? 'accept' : 'reject'} file. Please try again.`);
    }
};

const formatDate = (dateString) => {
    try {
        return new Date(dateString).toLocaleString();
    } catch (err) {
        console.error('Error formatting date:', err);
        return dateString; // Return original string if formatting fails
    }
};

// Clear search query
const clearSearch = () => {
    searchQuery.value = '';
};
</script>

<template>
    <DefaultLayout>
        <div class="container py-4">
            <Notification :success="success" :error="error" />
        </div>
        <div class="container py-4">
            <div class="bg-white p-4 rounded shadow-sm">
                <h1 class="text-center">Requested Files</h1>

                <!-- Search Bar -->
                <div class="row g-3 mt-3 mb-4" v-if="requestedFiles.length > 0">
                    <div class="col-md-6 col-lg-4 mx-auto">
                        <div class="input-group">
                            <input 
                                v-model="searchQuery" 
                                type="text" 
                                class="form-control" 
                                placeholder="Search by File Code" 
                                aria-label="Search files"
                            >
                            <button 
                                class="btn btn-outline-secondary" 
                                type="button"
                                @click="clearSearch"
                                v-if="searchQuery"
                            >
                                <i class="bi bi-x-circle"></i>
                            </button>
                            <button class="btn btn-primary" type="button">
                                <i class="bi bi-search me-1"></i> Search
                            </button>
                        </div>
                    </div>
                </div>

                <div v-if="isLoading" class="text-center my-4">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                    <p class="mt-2">Loading requested files...</p>
                </div>

                <div v-else-if="requestedFiles.length === 0" class="text-center my-4">
                    <em>No requested files.</em>
                </div>

                <div v-else-if="filteredFiles.length === 0" class="text-center my-4">
                    <div class="alert alert-info">
                        <i class="bi bi-info-circle me-2"></i>
                        No files match your search for "{{ searchQuery }}"
                        <button @click="clearSearch" class="btn btn-sm btn-outline-primary ms-3">
                            Clear Search
                        </button>
                    </div>
                </div>

                <div v-else class="table-responsive mt-4">
                    <div class="card shadow-sm border-0 rounded-3">
                        <div class="card-header fw-bold fs-6 d-flex justify-content-between align-items-center">
                            <span>Files Pending Acceptance</span>
                            <span class="badge bg-info text-dark">{{ filteredFiles.length }} file(s)</span>
                        </div>
                        <div class="card-body p-0">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-light">
                                    <tr class="text-center">
                                        <th>#</th>
                                        <th>File Code</th>
                                        <th>Status</th>
                                        <th>Requesting Department</th>
                                        <th>Request Date</th>
                                        <th>Request Notes</th>
                                        <th>Your Notes</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="(file, index) in filteredFiles" :key="`${file.fileCode}-${file.requestingDepartmentId}`"
                                        :class="index % 2 === 0 ? 'table-white' : 'table-light'">
                                        <td class="text-center fw-bold">{{ index + 1 }}</td>
                                        <td class="text-nowrap">{{ file.fileCode }}</td>
                                        <td>
                                            <span class="badge bg-warning text-dark">{{ file.status }}</span>
                                        </td>
                                        <td>{{ file.requestingDepartmentName }}</td>
                                        <td class="text-nowrap text-secondary">{{ formatDate(file.requestTime) }}</td>
                                        <td class="text-wrap">{{ file.notes }}</td>
                                        <td>
                                            <textarea v-model="notes[file.fileCode]" class="form-control" rows="1"
                                                placeholder="Add notes here (optional)"></textarea>
                                        </td>
                                        <td class="text-center">
                                            <div class="btn-group" role="group">
                                                <button @click="handleFileAction(file, true)"
                                                    class="btn btn-success btn-sm">
                                                    <i class="bi bi-check me-2"></i>
                                                    Accept
                                                </button>
                                                <button 
                                                    @click="handleFileAction(file, false)" 
                                                    class="btn btn-danger btn-sm">
                                                    <i class="bi bi-x-circle me-2"></i>
                                                    Reject
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </DefaultLayout>
</template>

<style scoped>
.table th,
.table td {
    padding: 0.75rem;
    vertical-align: middle;
}

.btn-group .btn {
    margin: 0 2px;
}

.badge {
    font-size: 0.85rem;
    padding: 0.4rem 0.6rem;
}

.spinner-border {
    width: 3rem;
    height: 3rem;
}

.alert {
    display: flex;
    align-items: center;
}
</style>