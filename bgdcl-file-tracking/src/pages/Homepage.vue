<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import Header from '../components/Header.vue';
import Footer from '../components/Footer.vue';
import LeftMenu from '../components/LeftMenu.vue';
import Notification from '../components/Notification.vue';
import TransactionModal from '../components/TransactionModal.vue';
import FileService from '../services/FileService';
import { getUserInfo } from '../services/LoginService';
import CurrentFiles from '../components/CurrentFiles.vue';

// Define the reactive references
const success = ref('');
const error = ref('');
const searchQuery = ref('');
const loading = ref(false);
const userInfo = ref(null);
const fileInfo = ref(null);
const isFileFound = ref(false);
const transferFileData = ref({
  fileCode: "",
  fromDepartmentId: '',
  toDepartmentId: '',
  fromUserId: '',
  toUserId: '',
  remarks: ""
});
const requestNote = ref('');

const transactionHistory = ref([]);
const showHistory = ref(false);
const isLoadingHistory = ref(false);

// Modal toggle states
const isModalOpen = ref(false);
const isTransactionModalOpen = ref(false);
const transactionType = ref('');

const acceptFilesCount = ref(0);
const requestedFilesCount = ref(0);
const isMenuCollapsed = ref(false);

// Add this to track current user for the left menu
const currentUser = ref(null);
const isFileRequested = ref(false);

// Fix: Eliminated redundant fileReturnable ref since we have isFileReturnable computed property
// This removes the bug where the two could get out of sync

const handleMenuCollapse = (collapsed) => {
  isMenuCollapsed.value = collapsed;
};

// Improved user info loading function with better error handling
const loadUserInfo = async () => {
  try {
    loading.value = true;
    const storedUserInfo = getUserInfo(localStorage.getItem('authToken'));
    
    if (!storedUserInfo) {
      console.warn('No user info found in localStorage');
      return;
    }
    
    // Handle both Promise and direct object scenarios
    if (storedUserInfo && typeof storedUserInfo === 'object' && typeof storedUserInfo.then === 'function') {
      console.log('userInfo is a Promise, awaiting resolution...');
      userInfo.value = await storedUserInfo;
    } else {
      userInfo.value = storedUserInfo;
    }
    
    // Set current user for left menu only when we have valid data
    if (userInfo.value && userInfo.value.userId) {
      currentUser.value = userInfo.value;
      console.log('User info loaded:', userInfo.value);
    } else {
      console.warn('Loaded user info is incomplete');
    }
  } catch (err) {
    console.error('Error loading user info:', err);
    showError('Failed to load user information');
  } finally {
    loading.value = false;
  }
};

// Improved function with error handling
const getToAcceptFilesCount = async () => {
  if (!userInfo.value?.departmentId) {
    console.warn('User info not available yet, cannot get files count');
    return;
  }
  
  try {
    const response = await FileService.getToAcceptFilesList(userInfo.value.departmentId);
    if (Array.isArray(response)) {
      acceptFilesCount.value = response.length;
      console.log('To accept files count:', acceptFilesCount.value);
    } else {
      console.warn('Invalid response format for to-accept files');
    }
  } catch (err) {
    console.error('Error loading to accept files count:', err);
  }
};

const takeBackFile = async (file) => {
  if (!file?.fileCode || !userInfo.value?.departmentId || !userInfo.value?.userId) {
    showError('Missing file or user information');
    return;
  }

  const payload = {
    fileCode: file.fileCode,
    fromDepartmentId: userInfo.value.departmentId,
  };

  try {
    const response = await FileService.takeBackFile(payload);

    if (response && !response.error) {
      showSuccess('File taken back successfully');
    } else {
      showError('Failed to take back file. Please try again.');
    }
    await getToAcceptFilesCount();
    await getRequestedFilesCount();
    await handleSearch();
  } catch (err) {
    console.error('Error returning file:', err);
    showError('Failed to return file. Please try again.');
  }
};

// Call loadUserInfo when component is mounted
onMounted(async () => {
  await loadUserInfo();
  
  // Only call these methods if userInfo was successfully loaded
  if (userInfo.value?.departmentId) {
    await Promise.all([
      getToAcceptFilesCount(),
      getRequestedFilesCount()
    ]);
  }
});

// Optimized watcher that only triggers when necessary
watch(() => userInfo.value?.departmentId, async (newValue, oldValue) => {
  if (newValue && newValue !== oldValue) {
    await getToAcceptFilesCount();
  }
}, { immediate: false });

// Success message handler
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

// Handle search logic
const handleSearch = async () => {
  showHistory.value = false;
  
  try {
    if (!searchQuery.value.trim()) {
      showError('Please enter a Customer Old Code');
      return;
    }

    loading.value = true;
    showSuccess('Search initiated for: ' + searchQuery.value);

    // Reset file found state at the beginning of search
    isFileFound.value = false;
    fileInfo.value = null;
    isFileRequested.value = false;

    // Perform the search logic - call the API
    const response = await FileService.getFileStatus(searchQuery.value.trim());

    // Check if response contains the error message
    if (response && response.error) {
      // File not found (error response from server)
      showError('File not found. Please check the code and try again.');
    } else {
      // If no error in the response, the file is found
      fileInfo.value = response;
      isFileFound.value = true; // Mark file as found
      
      // Check if the file has been requested from this department
      if (fileInfo.value.fileRequests && Array.isArray(fileInfo.value.fileRequests)) {
        checkFileRequestStatus(fileInfo.value.fileRequests);
      }

      showSuccess('File found successfully');
    }
  } catch (err) {
    // Handle errors during the API call
    if (err.response) {
      if (err.response.status === 404) {
        showError('File not found. Please check the code and try again.');
      } else {
        showError(`Error: ${err.response.status}. File Not Found.`);
      }
    } else {
      showError('Network or server error. Please try again later.');
    }
  } finally {
    loading.value = false;
  }
};

const getRequestedFilesCount = async () => {
  if (!currentUser.value?.departmentId) {
    console.warn('User department info not available, cannot get requested files count');
    return;
  }
  
  try {
    const response = await FileService.getRequestedFilesList(currentUser.value.departmentId);
    if (Array.isArray(response)) {
      requestedFilesCount.value = response.length;
      console.log('Requested files count:', requestedFilesCount.value);
    } else {
      console.warn('Invalid response format for requested files');
    }
  } catch (err) {
    console.error('Error loading requested files count:', err);
  }
};

// Improved function with better error handling
const checkFileRequestStatus = (fileRequests) => {
  try {
    if (!fileRequests || !Array.isArray(fileRequests) || !currentUser.value?.departmentId) {
      console.warn('File requests data invalid or missing user department');
      isFileRequested.value = false;
      return;
    }

    // Check if the file has been requested from this department
    isFileRequested.value = fileRequests.some(
      request => request.requestingDepartmentId === currentUser.value.departmentId
    );
    
    console.log('File request status:', isFileRequested.value);
  } catch (err) {
    console.error('Error checking file request status:', err);
    isFileRequested.value = false;
  }
};

// Toggle standard modal visibility
const toggleModal = () => {
  isModalOpen.value = !isModalOpen.value;
};

// Open transaction modal
const openTransactionModal = (type) => {
  if (!fileInfo.value) {
    showError('No file information available');
    return;
  }

  transactionType.value = type;
  isTransactionModalOpen.value = true;

  // Prevent scrolling on the body when modal is open
  document.body.style.overflow = 'hidden';
};

// Close transaction modal
const closeTransactionModal = () => {
  isTransactionModalOpen.value = false;

  // Re-enable scrolling on the body when modal is closed
  document.body.style.overflow = '';
};

// Handle transaction form submission with improved error handling
const handleTransactionSubmit = async (formData) => {
  try {
    loading.value = true;

    console.log('Submitting transaction:', formData);

    if (!formData.fileId || !userInfo.value?.departmentId || !userInfo.value?.userId) {
      showError('Missing required transaction data');
      return;
    }

    if (formData.transactionType === 'in') {
      transferFileData.value = {
        fileCode: formData.fileId,
        fromDepartmentId: formData.department,
        toDepartmentId: userInfo.value.departmentId,
        fromUserId: formData.handlerName,
        toUserId: userInfo.value.userId,
        remarks: formData.notes
      };
    } else {
      transferFileData.value = {
        fileCode: formData.fileId,
        fromDepartmentId: userInfo.value.departmentId,
        toDepartmentId: formData.department,
        fromUserId: userInfo.value.userId,
        toUserId: formData.handlerName,
        remarks: formData.notes
      };
    }

    const response = await FileService.transferFile(transferFileData.value);

    if (response && response.error) {
      showError(`Failed to submit transaction: ${response.error}`);
      return;
    }

    // After successful submission
    showSuccess(`File ${formData.transactionType === 'in' ? 'checked in' : 'checked out'} successfully`);
    closeTransactionModal();

    // Refresh file data
    if (fileInfo.value?.fileCode) {
      refreshFileData(fileInfo.value.fileCode);
    }
  } catch (err) {
    console.error('Error submitting transaction:', err);
    showError('Failed to submit transaction. Please try again.');
  } finally {
    loading.value = false;
  }
};

// New function to consolidate file data refresh logic
const refreshFileData = async (fileCode) => {
  try {
    const updatedFileResponse = await FileService.getFileStatus(fileCode);
    if (updatedFileResponse && !updatedFileResponse.error) {
      fileInfo.value = updatedFileResponse;
      
      // Check if file has been requested from this department
      if (updatedFileResponse.fileRequests && Array.isArray(updatedFileResponse.fileRequests)) {
        checkFileRequestStatus(updatedFileResponse.fileRequests);
      }
      
      // Refresh transaction history if it's currently shown
      if (showHistory.value) {
        await loadTransactionHistory(fileCode);
      }
    }
  } catch (err) {
    console.error('Error refreshing file data:', err);
  }
};

// New function to consolidate transaction history loading
const loadTransactionHistory = async (fileCode) => {
  try {
    isLoadingHistory.value = true;
    const historyResponse = await FileService.getTransactionHistory(fileCode);
    if (Array.isArray(historyResponse)) {
      transactionHistory.value = historyResponse;
    } else {
      console.warn('Invalid transaction history response format');
      transactionHistory.value = [];
    }
  } catch (err) {
    console.error('Error loading transaction history:', err);
    showError('Failed to load transaction history');
    transactionHistory.value = [];
  } finally {
    isLoadingHistory.value = false;
  }
};

// Handle validation errors from modal
const handleValidationError = (message) => {
  showError(message);
};

// Optimized transaction history toggle
const showTransactionHistory = async (fileCode) => {
  try {
    // Toggle display state
    showHistory.value = !showHistory.value;
    
    // Only load data if we're showing history and don't have data yet
    if (showHistory.value && (!transactionHistory.value.length || fileInfo.value?.fileCode !== fileCode)) {
      await loadTransactionHistory(fileCode);
    }
  } catch (err) {
    console.error('Error handling transaction history:', err);
    showError('Failed to handle transaction history request');
  }
};

// Accept file with improved validation
const acceptFile = async () => {
  if (!fileInfo.value?.fileCode || !userInfo.value?.departmentId || !userInfo.value?.userId) {
    showError('Missing file or user information');
    return;
  }

  const payload = {
    fileCode: fileInfo.value.fileCode,
    departmentId: userInfo.value.departmentId,
    userId: userInfo.value.userId,
    accept: true,
    notes: "Accepted after review"
  };

  console.log('Accept file payload:', payload);

  try {
    loading.value = true;
    const response = await FileService.acceptFile(payload);
    
    if (response && !response.error) {
      fileInfo.value = response;
      showSuccess('File accepted successfully');
      
      // Refresh pending files count
      await getToAcceptFilesCount();
    } else {
      showError('Failed to accept file. Please try again.');
    }
  } catch (err) {
    console.error('Error accepting file:', err);
    showError('Failed to accept file. Please try again.');
  } finally {
    loading.value = false;
  }
};

// Request file with improved validation
const requestFile = async (file) => {
  if (!file?.fileCode || !userInfo.value?.departmentId || !userInfo.value?.userId) {
    showError('Missing file or user information');
    return;
  }

  if (!file.currentDepartmentId) {
    showError('Cannot determine current department of file');
    return;
  }

  const payload = {
    fileCode: file.fileCode,
    requestingDepartmentId: userInfo.value.departmentId,
    requestedFromDepartmentId: file.currentDepartmentId,
    requestingUserId: userInfo.value.userId,
    notes: requestNote.value.trim() || "File requested" // Default note if empty
  };

  console.log('Request file payload:', payload);

  try {
    loading.value = true;
    const response = await FileService.requestFile(payload);
    
    if (response && !response.error) {
      fileInfo.value = response;
      isFileRequested.value = true;
      showSuccess('File requested successfully');
      
      // Clear request note after successful request
      requestNote.value = '';
      
      // Refresh requested files count
      await getRequestedFilesCount();
    } else {
      showError('Failed to request file. Please try again.');
    }
  } catch (err) {
    console.error('Error requesting file:', err);
    showError('Failed to request file. Please try again.');
  } finally {
    loading.value = false;
  }
};

// Improved computed property for department matching
const isDepartmentMatching = computed(() => {
  return userInfo.value?.departmentId && 
         fileInfo.value?.currentDepartmentId && 
         userInfo.value.departmentId === fileInfo.value.currentDepartmentId;
});

// Fixed and improved isFileReturnable computed property
const isFileReturnable = computed(() => {
  // Check for required data
  if (!userInfo.value?.departmentId || !fileInfo.value?.currentDepartmentId) {
    return false;
  }
  
  // Check if file is not yet received in another department (pending status)
  if (fileInfo.value.currentDepartmentId !== userInfo.value.departmentId && 
      fileInfo.value.status === "pending") {
    
    // Check if the file was sent from current user's department
    const lastTransaction = fileInfo.value.lastTransaction;
    if (lastTransaction && lastTransaction.fromDepartmentId === userInfo.value.departmentId) {
      console.log('File is returnable:');
      console.log('- file currentDepartmentId:', fileInfo.value.currentDepartmentId);
      console.log('- user departmentId:', userInfo.value.departmentId);
      console.log('- lastTransaction fromDepartmentId:', lastTransaction.fromDepartmentId);
      console.log('- status:', fileInfo.value.status);
      return true;
    }
  }
  
  return false;
});

// Optimized watchers that use specific paths instead of deep watching
watch(() => userInfo.value?.departmentId, (newVal) => {
  if (newVal) {
    console.log('User department changed:', newVal);
  }
});

watch(() => fileInfo.value?.currentDepartmentId, (newVal) => {
  if (newVal) {
    console.log('File department changed:', newVal);
  }
});
</script>

<template>
  <div class="app-container">
    <!-- Header component -->
    <Header />

    <!-- Left Menu -->
    <LeftMenu :acceptFilesCount="acceptFilesCount" :requestedFilesCount="requestedFilesCount"
      :username="currentUser?.fullName || ''" @toggle-collapse="handleMenuCollapse" />

    <!-- Main content container -->
    <div class="main-content" :class="{ 'menu-collapsed': isMenuCollapsed }">
      <!-- Notification area -->
      <div class="container py-4">
        <Notification :success="success" :error="error" />
      </div>

      <!-- Main content -->
      <div class="container bg-white p-4 rounded shadow-sm">
        <h2 class="fw-bold mb-4">Search Files</h2>

        <!-- Search bar and button -->
        <div class="row g-3">
          <div class="col-md-9 col-12">
            <input v-model="searchQuery" type="text" placeholder="Enter Customer Old Code" class="form-control"
              aria-label="Search query" @keyup.enter="handleSearch" />
          </div>
          <div class="col-md-3 col-12">
            <button type="button" class="btn btn-primary d-flex align-items-center justify-content-center w-100"
              aria-label="Search files" @click="handleSearch" :disabled="loading">
              <i class="bi bi-search me-2"></i>
              <span v-if="loading">Searching...</span>
              <span v-else>Search</span>
            </button>
          </div>
        </div>

        <!-- Card view if file is found -->
        <div v-if="isFileFound && fileInfo" class="card mt-4">
          <div class="card-body">
            <p>
              <strong>Customer Old Code:</strong> <span class="text-primary"><strong class="fs-6">{{ fileInfo.fileCode
                  }}</strong></span>
            </p>
            <p>
              <strong>Customer Name:</strong> <span class="text-info fs-6">{{ fileInfo.customerName }}</span>
            </p>
            <p>
              <strong>File is currently at:</strong> <span class="text-success fw-bold fs-6">{{
                fileInfo.currentDepartment }}</span>
            </p>
            <p>
              <strong>Customer New Code:</strong> <span class="text-warning fs-6">{{ fileInfo.customerNewCode }}</span>
            </p>
            <p>
              <strong>Customer Zone:</strong> <span class="text-danger fs-6">{{ fileInfo.zone }}</span>
            </p>

            <!-- Buttons -->
            <div class="d-flex flex-wrap align-items-start gap-3 mt-3">
              <!-- Accept File -->
              <button v-if="isDepartmentMatching && fileInfo.status === 'pending'" class="btn btn-success"
                @click="acceptFile" :disabled="loading">
                <i class="bi bi-box-arrow-right me-2"></i>
                {{ loading ? 'Processing...' : 'Accept File' }}
              </button>

              <!-- File Out -->
              <button v-if="isDepartmentMatching && fileInfo.status === 'in'" class="btn btn-danger"
                @click="openTransactionModal('out')" :disabled="loading">
                <i class="bi bi-box-arrow-left me-2"></i>
                File Out
              </button>

              <!-- Request Note + Request File -->
              <div v-if="!isDepartmentMatching && isFileRequested" class="d-flex flex-column" style="min-width: 300px;">
                <button class="btn btn-outline-danger" disabled>
                  <i class="bi bi-x-lg me-2"></i>
                  File Already Requested
                </button>
              </div>
              <div v-if="!isDepartmentMatching && !isFileRequested && !isFileReturnable" class="d-flex flex-column"
                style="min-width: 300px;">
                <textarea v-model="requestNote" class="form-control mb-2" rows="1"
                  placeholder="Write request note (optional)"></textarea>
                <button class="btn btn-primary" @click="requestFile(fileInfo)" :disabled="loading">
                  <i class="bi bi-box-arrow-right me-2"></i>
                  {{ loading ? 'Processing...' : 'Request File' }}
                </button>
              </div>

              <!-- File In -->
              <button v-if="isFileReturnable" class="btn btn-primary" @click="takeBackFile(fileInfo)" :disabled="loading"
              :title="`This file hasn't been received yet in ${fileInfo.value?.currentDepartment || 'this department'}`"
              >
                <i class="bi bi-box-arrow-right me-2"></i>
                File In
              </button>

              <!-- Show Movement History -->
              <button class="btn btn-outline-primary d-flex align-items-center"
                @click="showTransactionHistory(fileInfo.fileCode)" :disabled="isLoadingHistory">
                <i class="bi bi-clock-history me-2"></i>
                {{ isLoadingHistory ? 'Loading...' : (showHistory ? 'Hide' : 'Show') + ' Movement History' }}
              </button>
            </div>

            <!-- Transaction History Table -->
            <div v-if="showHistory" class="mt-4">
              <div v-if="isLoadingHistory" class="text-center py-3">
                <div class="spinner-border text-primary" role="status">
                  <span class="visually-hidden">Loading...</span>
                </div>
                <p class="mt-2">Loading transaction history...</p>
              </div>
              <div v-else-if="transactionHistory.length === 0">
                <div class="alert alert-info">
                  <i class="bi bi-info-circle me-2"></i>
                  <em>No transaction records found for this file.</em>
                </div>
              </div>
              <div v-else class="table-responsive">
                <div class="card shadow-sm border-0 rounded-3">
                  <div class="card-header fw-bold fs-6">
                    Movement History for {{ fileInfo.fileCode }}
                  </div>
                  <div class="card-body p-0">
                    <table class="table table-hover align-middle mb-0">
                      <thead class="table-light">
                        <tr class="text-center">
                          <th>#</th>
                          <th>Date & Time</th>
                          <th>From</th>
                          <th>To</th>
                          <th>From Dept</th>
                          <th>To Dept</th>
                          <th>Remarks</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="(txn, index) in transactionHistory" :key="txn.transactionId || index"
                          :class="index % 2 === 0 ? 'table-white' : 'table-light'">
                          <td class="text-center fw-bold">{{ index + 1 }}</td>
                          <td class="text-nowrap text-secondary">{{ new Date(txn.dateTime).toLocaleString() }}</td>
                          <td>
                            <strong>{{ txn.fromUser?.fullName || 'N/A' }}</strong><br />
                            <small class="text-muted">{{ txn.fromUser?.designation || '' }}<br />{{ txn.fromUser?.section || '' }}</small>
                          </td>
                          <td>
                            <strong>{{ txn.toUser?.fullName || 'N/A' }}</strong><br />
                            <small class="text-muted">{{ txn.toUser?.designation || '' }}<br />{{ txn.toUser?.section || '' }}</small>
                          </td>
                          <td>
                            <span class="badge bg-info text-dark">{{ txn.fromDepartment || 'N/A' }}</span>
                          </td>
                          <td>
                            <span class="badge bg-success">{{ txn.toDepartment || 'N/A' }}</span>
                          </td>
                          <td class="text-wrap">{{ txn.remarks || 'No remarks' }}</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Transaction Modal Component -->
        <TransactionModal :is-open="isTransactionModalOpen" :file-info="fileInfo" :transaction-type="transactionType"
          :loading="loading" @close="closeTransactionModal" @submit="handleTransactionSubmit"
          @validation-error="handleValidationError" />
      </div>

      <CurrentFiles v-if="userInfo?.departmentId" :departmentId="userInfo?.departmentId" :size="10" sort="lastUpdated"
        direction="desc" />

      <!-- Footer component -->
      <Footer />
    </div>
  </div>
</template>

<style scoped>
.app-container {
  display: flex;
  min-height: 100vh;
}

.main-content {
  flex: 1;
  margin-left: 250px;
  /* Matches the width of the expanded left menu */
  transition: margin-left 0.3s ease;
  padding-top: 76px;
  /* Height of the fixed header + some padding */
  min-height: 100vh;
  background-color: #f5f5f5;
}

.main-content.menu-collapsed {
  margin-left: 70px;
  /* Matches the width of the collapsed left menu */
}

@media (max-width: 768px) {
  .main-content {
    margin-left: 70px;
  }
}

/* Make sure the header is on top of everything */
::v-deep(.navbar) {
  z-index: 1050;
}

/* Make sure the left menu is below the header but above the content */
::v-deep(.left-menu) {
  z-index: 1040;
}
</style>