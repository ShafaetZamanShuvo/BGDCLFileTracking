<script setup>
import { ref, watch, onMounted } from 'vue';
import UtilService from '../services/UtilService';

// Props received from parent component
const props = defineProps({
    isOpen: Boolean,
    fileInfo: Object,
    transactionType: String,
    loading: Boolean
});

// Events to emit back to parent
const emit = defineEmits(['close', 'submit', 'validationError']);

// Transaction form data
const transactionFormData = ref({
    fileId: '',
    transactionType: '',
    dateTime: '',
    department: '',
    section: '',
    handlerName: '',
    notes: '',
    handlerId: ''
});


// Options for selects
const departments = ref([
]);

const sections = ref([
]);

const handlers = ref([
]);

const getAllUsers = async () => {
  try {
    const response = await UtilService.getAllUsers();
    handlers.value = response;
  } catch (err) {
    console.error('Error loading user info:', err);
  }
};

const getAllDepartments = async () => {
  try {
    const response = await UtilService.getAllDepartments();
    departments.value = response;
  } catch (err) {
    console.error('Error loading department info:', err);
  }
};

const getAllSections = async () => {
  try {
    const response = await UtilService.getAllSections();
    sections.value = response;
  } catch (err) {
    console.error('Error loading section info:', err);
  }
};




// Set default date time on mount
onMounted( () => {
    getAllDepartments();
    getAllUsers();
    getAllSections();
    setDefaultDateTime(); 
});

// Set default date time to current date and time
const setDefaultDateTime = () => {
    const now = new Date();
    // Format as YYYY-MM-DDThh:mm
    const formattedDateTime = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')}T${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`;
    transactionFormData.value.dateTime = formattedDateTime;
};

// Reset form data when modal opens
watch(() => props.isOpen, (isOpen) => {
    if (isOpen && props.fileInfo) {
        transactionFormData.value = {
            fileId: props.fileInfo.fileCode || '',
            transactionType: props.transactionType,
            dateTime: transactionFormData.value.dateTime, // Keep the current date/time
            department: '',
            section: '',
            handlerName: '',
            notes: '',
            handlerId : ''
        };
    }
});

// Close modal handler
const closeModal = () => {
    emit('close');
};

// Form submission handler
const handleSubmit = (event) => {
    event.preventDefault();

    // Basic validation
    if (!transactionFormData.value.dateTime) {
        emit('validationError', 'Please select a date and time');
        return;
    }

    if (props.transactionType === 'out' && !transactionFormData.value.department) {
        emit('validationError', 'Please select a department');
        return;
    }

    if (!transactionFormData.value.section) {
        emit('validationError', 'Please select a section');
        return;
    }

    if (!transactionFormData.value.handlerName) {
        emit('validationError', 'Please select a handler');
        return;
    }

    // Emit the submit event with form data
    emit('submit', transactionFormData.value);
};

// Add support for keyboard events (Escape key closing the modal)
const handleKeyDown = (event) => {
    if (event.key === 'Escape' && props.isOpen) {
        closeModal();
    }
};

// Add event listener for keydown events
onMounted(() => {
    window.addEventListener('keydown', handleKeyDown);
});

// Clean up event listener when component is unmounted
const onUnmounted = () => {
    window.removeEventListener('keydown', handleKeyDown);
};
</script>

<template>
    <!-- Transaction Form Modal (Bootstrap version) -->
    <div v-if="isOpen" class="modal fade show d-block" tabindex="-1" role="dialog" aria-modal="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="transactionFormTitle">
                        {{ transactionType === 'in' ? 'Check In File' : 'Check Out File' }}
                    </h5>
                    <button type="button" class="btn-close" @click="closeModal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="transactionForm" @submit="handleSubmit">
                        <!-- Hidden inputs -->
                        <input type="hidden" v-model="transactionFormData.fileId">
                        <input type="hidden" v-model="transactionFormData.transactionType">

                        <!-- File Information -->
                        <div class="card mb-3 bg-light">
                            <div class="card-body">
                                <p class="fw-bold mb-1">File: <span class="text-primary">{{ fileInfo?.fileCode }}</span>
                                </p>
                                <p class="fw-bold mb-1">Customer: <span class="text-secondary">{{ fileInfo?.customerName
                                        || 'N/A' }}</span></p>
                                <p class="fw-bold mb-1">
                                    Current Status:
                                    <span :class="{
                                        'text-success': transactionType?.toLowerCase() === 'out',
                                        'text-danger': transactionType?.toLowerCase() === 'in'
                                    }">
                                        {{ transactionType?.toLowerCase() === 'in' ? 'Out' :
                                            transactionType?.toLowerCase() === 'out' ? 'In' : 'Unknown' }}
                                    </span>
                                </p>
                                <p class="fw-bold mb-0">Current Department: <span class="text-success">{{
                                        fileInfo?.currentDepartment || 'Unknown' }}</span></p>
                            </div>
                        </div>

                        <!-- Form Fields -->
                        <div class="row g-3">
                            <!-- Date and Time -->
                            <div class="col-md-6">
                                <label for="dateTime" class="form-label">Date and Time</label>
                                <input type="datetime-local" class="form-control" id="dateTime"
                                    v-model="transactionFormData.dateTime" required>
                            </div>

                            <!-- Department Selection - Only shown for "Out" transactions -->
                            <div class="col-md-6">
                                <label for="departmentSelect" class="form-label"> {{ transactionType === 'out' ? 'To' : 'From' }} Department</label>
                                <select class="form-select" id="departmentSelect"
                                    v-model="transactionFormData.department" required>
                                    <option value="">Select Department</option>
                                    <option v-for="dept in departments" :key="dept.id" :value="dept.id">
                                        {{ dept.name }}
                                    </option>
                                </select>
                            </div>

                            <!-- Section Selection -->
                            <div class="col-md-6">
                                <label for="sectionSelect" class="form-label">{{ transactionType === 'out' ? 'To ' : 'From ' }}Section</label>
                                <select class="form-select" id="sectionSelect" v-model="transactionFormData.section"
                                    required>
                                    <option value="">Select Section</option>
                                    <option v-for="section in sections" :key="section.id" :value="section.id">
                                        {{ section.name }}
                                    </option>
                                </select>
                            </div>

                            <!-- Handler Selection -->
                            <div class="col-md-6">
                                <label for="handlerSelect" class="form-label"> {{ transactionType === 'out' ? 'To ' : 'From ' }}Officer</label>
                                <select class="form-select" id="handlerSelect" v-model="transactionFormData.handlerName"
                                    required>
                                    <option value="">Select Handler</option>
                                    <option v-for="handler in handlers" :key="handler.userId" :value="handler.userId">
                                        {{ handler.fullName }} ({{ handler.department }})
                                    </option>
                                </select>
                            </div>

                            <!-- Notes -->
                            <div class="col-12">
                                <label for="transactionNotes" class="form-label">Notes</label>
                                <textarea class="form-control" id="transactionNotes" v-model="transactionFormData.notes"
                                    rows="3" placeholder="Enter transaction notes"></textarea>
                            </div>
                        </div>

                        <!-- Form Buttons -->
                        <div class="d-flex justify-content-end gap-2 mt-4">
                            <button type="button" class="btn btn-secondary" @click="closeModal">Cancel</button>
                            <button type="submit" class="btn btn-primary" :disabled="loading">
                                <span v-if="loading">Processing...</span>
                                <span v-else>Submit</span>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Modal backdrop -->
        <!-- <div class="modal-backdrop fade show"></div> -->
    </div>
</template>