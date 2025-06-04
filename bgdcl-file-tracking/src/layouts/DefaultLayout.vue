<script setup>
import { ref, onMounted } from 'vue';
import Header from '../components/Header.vue';
import LeftMenu from '../components/LeftMenu.vue';
import Footer from '../components/Footer.vue';
import { getUserInfo } from '../services/LoginService.js';
import FileService from '../services/FileService.js';

const currentUser = ref(null);
const acceptFilesCount = ref(0);
const requestedFilesCount = ref(0);
const isMenuCollapsed = ref(false);

const handleMenuCollapse = (collapsed) => {
  isMenuCollapsed.value = collapsed;
};

const getToAcceptFilesCount = async () => {
  try {
    const response = await FileService.getToAcceptFilesList(currentUser.value.departmentId);
    acceptFilesCount.value = response.length;
    console.log('To accept files count:', acceptFilesCount.value);
  } catch (err) {
    console.error('Error loading to accept files count:', err);
  }
};

const getRequestedFilesCount = async () => {
  try {
    const response = await FileService.getRequestedFilesList(currentUser.value.departmentId);
    requestedFilesCount.value = response.length;
    console.log('Requested files count:', requestedFilesCount.value);
  } catch (err) {
    console.error('Error loading requested files count:', err);
  }
}

const loadUserInfo = async () => {
    try {
    const token = localStorage.getItem('authToken');
    if (token) {
      currentUser.value = await getUserInfo(token);
    }
  } catch (err) {
    console.error('Error loading user info:', err);
  }
}

// Load user info on mount
onMounted(async () => {
  await loadUserInfo();
  await getToAcceptFilesCount();
  await getRequestedFilesCount();
});
</script>

<template>
  <div class="app-container">
    <!-- Header component -->
    <Header />
    
    <!-- Left Menu -->
    <LeftMenu 
      :acceptFilesCount="acceptFilesCount"
      :requestedFilesCount="requestedFilesCount"
      :username="currentUser?.fullName || ''"
      @toggle-collapse="handleMenuCollapse"
    />

    <!-- Main content container -->
    <div class="main-content" :class="{ 'menu-collapsed': isMenuCollapsed }">
      <!-- Content slot -->
      <slot></slot>
      
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
  margin-left: 250px; /* Matches the width of the expanded left menu */
  transition: margin-left 0.3s ease;
  padding-top: 76px; /* Height of the fixed header + some padding */
  min-height: 100vh;
  background-color: #f5f5f5;
}

.main-content.menu-collapsed {
  margin-left: 70px; /* Matches the width of the collapsed left menu */
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