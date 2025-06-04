<script setup>
import { ref, computed, watchEffect, onMounted } from 'vue';
import FileService from '../services/FileService';
import Notification from './Notification.vue';

const props = defineProps({
    departmentId: Number,
    size: {
        type: Number,
        default: 10
    },
    sort: {
        type: String,
        default: 'lastUpdated'
    },
    direction: {
        type: String,
        default: 'desc'
    }
});

const files = ref([]);
const currentPage = ref(0);
const totalPages = ref(0);
const isLoading = ref(true);
const success = ref('');
const error = ref('');

const showSuccess = (msg) => {
    error.value = '';
    success.value = msg;
    setTimeout(() => (success.value = ''), 3000);
};

const showError = (msg) => {
    success.value = '';
    error.value = msg;
    setTimeout(() => (error.value = ''), 3000);
};

const fetchFiles = async () => {
    try {
        isLoading.value = true;
        const res = await FileService.getFilesByDepartment({
            departmentId: props.departmentId,
            page: currentPage.value,
            size: props.size,
            sort: props.sort,
            direction: props.direction
        });
        files.value = res.files;
        totalPages.value = res.totalPages;
    } catch (err) {
        showError('Failed to fetch files');
    } finally {
        isLoading.value = false;
    }
};

onMounted(fetchFiles);
watchEffect(fetchFiles);

const prevPage = () => {
    if (currentPage.value > 0) currentPage.value--;
};

const nextPage = () => {
    if (currentPage.value < totalPages.value - 1) currentPage.value++;
};

const formatDate = (str) => new Date(str).toLocaleString();
</script>

<template>
    <div class="container py-4">
        <Notification :success="success" :error="error" />

        <div class="bg-white p-4 rounded shadow-sm">
            <h1 class="text-center">Files</h1>

            <div v-if="isLoading" class="text-center my-4">
                <div class="spinner-border text-primary" role="status"></div>
                <p class="mt-2">Loading files...</p>
            </div>

            <div v-else>
                <div class="table-responsive mt-4">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr class="text-center">
                                <th>#</th>
                                <th>File Code</th>
                                <th>Status</th>
                                <th>Last Updated</th>
                                <th>Customer</th>
                                <th>Zone</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(file, index) in files" :key="file.id">
                                <td class="text-center">{{ index + 1 + currentPage * props.size }}</td>
                                <td class="text-center">{{ file.oldCode }}</td>
                                <td class="text-center"><span class="badge bg-success text-dark">{{ file.currentStatus }}</span></td>
                                <td class="text-nowrap text-secondary text-center">{{ formatDate(file.lastUpdated) }}</td>
                                <td class="text-center">{{ file.customerName }} ({{ file.customerCode }})</td>
                                <td class="text-center">{{ file.zone }}</td>
                                <td class="text-center">
                                    <router-link :to="`/files/${file.id}`" class="btn btn-sm btn-info"><span><i class="bi bi-eye"></i></span></router-link>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="d-flex justify-content-between align-items-center mt-3">
                    <button @click="prevPage" class="btn btn-outline-secondary" :disabled="currentPage === 0">
                        Previous
                    </button>

                    <span>Page {{ currentPage + 1 }} of {{ totalPages }}</span>

                    <button @click="nextPage" class="btn btn-outline-secondary"
                        :disabled="currentPage >= totalPages - 1">
                        Next
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.table th,
.table td {
    padding: 0.75rem;
    vertical-align: middle;
}

.spinner-border {
    width: 2.5rem;
    height: 2.5rem;
}

.badge {
    font-size: 0.85rem;
    padding: 0.4rem 0.6rem;
}
</style>
