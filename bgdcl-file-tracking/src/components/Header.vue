<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { getUserInfo } from "../services/LoginService.js";

const currentUser = ref(null);
const router = useRouter();

const logout = () => {
  localStorage.removeItem("authToken");
  router.push({ name: "Login" });
};

const getUser = async () => {
  const token = localStorage.getItem("authToken");
  if (token) {
    try {
      currentUser.value = await getUserInfo(token);
      console.log("Current user:", currentUser.value);
    } catch (err) {
      console.error("Error fetching user info:", err);
      currentUser.value = null;
    }
  } else {
    currentUser.value = null;
  }
};

onMounted(() => {
  getUser();
});
</script>

<template>
  <header>
    <nav class="navbar fixed-top navbar-expand-lg navbar-light bg-light">
      <div class="container-fluid">
        <!-- Logo -->
        <img src="../assets/bgdcl.jpeg" alt="Bakhrabad Gas Distribution Company Logo" width="40" height="40"
          class="d-inline-block align-text-top" />

        <!-- Brand Name -->
        <span class="mx-2"></span>
        <router-link to="/home" class="navbar-brand">Customer File Tracking</router-link>

        <!-- Toggle button for mobile -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
          aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Navbar Content -->
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav ms-auto align-items-center">
            <!-- Greeting -->
            <li class="nav-item me-3" v-if="currentUser">
              <div class="navbar-text">
                <div>Hello, {{ currentUser.fullName }}!</div>
                <div class="small text-muted">
                  {{ currentUser.designation }}, {{ currentUser.department }}
                </div>
              </div>
            </li>

            <!-- Logout Button -->
            <li class="nav-item">
              <button @click="logout" class="btn btn-outline-secondary" type="button">
                Logout
              </button>
            </li>
          </ul>

        </div>
      </div>
    </nav>
  </header>
</template>

<style scoped>
.navbar-text {
  font-weight: 500;
}
</style>
