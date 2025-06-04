<template>
    <div class="notification-container">
      <div
        v-if="success"
        class="alert alert-success alert-dismissible fade show"
        role="alert"
      >
        {{ success }}
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="alert"
          aria-label="Close"
        ></button>
      </div>
      <div
        v-if="error"
        class="alert alert-danger alert-dismissible fade show"
        role="alert"
      >
        {{ error }}
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="alert"
          aria-label="Close"
        ></button>
      </div>
    </div>
  </template>
  
  <script setup>
  import { defineProps } from 'vue';
  
  const props = defineProps({
    success: String,
    error: String,
  });
  </script>
  
  <style scoped>
  .notification-container {
    position: fixed;
    top: 10px;  /* Adjust as needed */
    left: 50%;
    transform: translateX(-50%);
    z-index: 1050; /* Higher than navbar's z-index */
    width: 100%;
    max-width: 500px; /* Optional: limit the width */
  }
  
  .alert {
    margin-bottom: 1rem;
  }
  </style>
  