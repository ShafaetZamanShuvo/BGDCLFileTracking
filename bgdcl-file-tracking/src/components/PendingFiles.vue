<script setup>
import { ref, onMounted, computed } from 'vue';
import DefaultLayout from '../layouts/DefaultLayout.vue';
import FileService from '../services/FileService';
import { getUserInfo } from '../services/LoginService';
import Notification from './Notification.vue';

const success = ref('');
const error = ref('');
const pendingFiles = ref([]);
const currentUser = ref(null);
const isLoading = ref(true);
const notes = ref({});
const searchTerm = ref(''); // Added search term ref

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

// Computed property for filtered files based on search term
const filteredFiles = computed(() => {
  if (!searchTerm.value) return pendingFiles.value;
  
  return pendingFiles.value.filter(file => 
    file.oldCode.toLowerCase().includes(searchTerm.value.toLowerCase())
  );
});

onMounted(async () => {
    try {
        currentUser.value = await getUserInfo(localStorage.getItem('authToken'));
        pendingFiles.value = await FileService.getToAcceptFilesList(currentUser.value.departmentId);
    } catch (err) {
        console.error('Error loading pending files:', err);
    } finally {
        isLoading.value = false;
    }
});

const handleFileAction = async (file, accept) => {
    try {
        const payload = {
            fileCode: file.oldCode,
            departmentId: currentUser.value.departmentId,
            userId: currentUser.value.id,
            accept: accept,
            notes: notes.value[file.id] || ''
        };

        await FileService.acceptFile(payload);

        pendingFiles.value = pendingFiles.value.filter(f => f.id !== file.id);

        showSuccess(`File ${file.oldCode} has been ${accept ? 'accepted' : 'rejected'}`);
        //reload page after 3 seconds
        setTimeout(() => {
            window.location.reload();
        }, 3000);
        
    } catch (err) {
        console.error(`Error ${accept ? 'accepting' : 'rejecting'} file:`, err);
        showError(`Failed to ${accept ? 'accept' : 'reject'} file. Please try again.`);
    }
};

const formatDate = (dateString) => {
    return new Date(dateString).toLocaleString();
};

// Clear search term function
const clearSearch = () => {
    searchTerm.value = '';
};
</script>

<template>
    <DefaultLayout>
        <div class="container py-4">
            <Notification :success="success" :error="error" />
        </div>
        <div class="container py-4">
            <div class="bg-white p-4 rounded shadow-sm">
                <h1 class="text-center">Pending Files</h1>

                <div v-if="isLoading" class="text-center my-4">
                    Loading pending files...
                </div>

                <div v-else-if="pendingFiles.length === 0" class="text-center my-4">
                    <em>No pending files to accept.</em>
                </div>

                <div v-else>
                    <!-- Search input field -->
                    <div class="mb-4 mt-3">
                        <div class="input-group">
                            <span class="input-group-text">
                                <i class="bi bi-search"></i>
                            </span>
                            <input
                                type="text"
                                class="form-control"
                                v-model="searchTerm"
                                placeholder="Search by file code..."
                                aria-label="Search files"
                            />
                            <button 
                                v-if="searchTerm" 
                                class="btn btn-outline-secondary" 
                                type="button"
                                @click="clearSearch"
                            >
                                <i class="bi bi-x-lg"></i>
                            </button>
                        </div>
                    </div>

                    <div class="table-responsive mt-3">
                        <div class="card shadow-sm border-0 rounded-3">
                            <div class="card-header fw-bold fs-6 d-flex justify-content-between align-items-center">
                                <span>Files Pending Acceptance</span>
                                <span class="badge bg-primary">{{ filteredFiles.length }} of {{ pendingFiles.length }} files</span>
                            </div>
                            <div class="card-body p-0">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="table-light">
                                        <tr class="text-center">
                                            <th>#</th>
                                            <th>File Code</th>
                                            <th>Status</th>
                                            <th>Last Updated</th>
                                            <th>Notes</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr v-for="(file, index) in filteredFiles" :key="file.id"
                                            :class="index % 2 === 0 ? 'table-white' : 'table-light'">
                                            <td class="text-center fw-bold">{{ index + 1 }}</td>
                                            <td class="text-nowrap">{{ file.oldCode }}</td>
                                            <td>
                                                <span class="badge bg-warning text-dark">{{ file.currentStatus }}</span>
                                            </td>
                                            <td class="text-nowrap text-secondary">{{ formatDate(file.lastUpdated) }}</td>
                                            <td>
                                                <textarea v-model="notes[file.id]" class="form-control" rows="2"
                                                    placeholder="Add notes here (optional)">
                                                </textarea>
                                            </td>
                                            <td class="text-center">
                                                <div class="btn-group" role="group">
                                                    <button @click="handleFileAction(file, true)"
                                                        class="btn btn-success btn-sm">
                                                        <i class="bi bi-box-arrow-right me-2"></i>
                                                        Accept
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr v-if="filteredFiles.length === 0">
                                            <td colspan="6" class="text-center py-3">
                                                <em>No files match your search criteria.</em>
                                                <div class="mt-2">
                                                    <button @click="clearSearch" class="btn btn-sm btn-outline-secondary">
                                                        Clear search
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
</style>