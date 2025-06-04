<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import {login,getUserInfo} from "../services/LoginService.js";

const credentials = ref({
  username: "",
  password: "",
});

const error = ref(null); // Ref to store error message
const router = useRouter();

const submitForm = async (event) => {
  event.preventDefault(); // Prevent default form submission
  error.value = null; // Reset error message before submitting

  try {
    const { username, password } = credentials.value;
    const response = await login(username, password);

    if (!response) {
      error.value = "Invalid username or password.";
      return;
    }

    if (response) {
      localStorage.setItem("authToken", response.accessToken);
      localStorage.setItem("user", JSON.stringify(response));
      localStorage.setItem("userInfo", JSON.stringify(getUserInfo(response.accessToken)) );
    }
    router.push({ name: "Homepage" });
  } catch (err) {
    console.error("Login failed:", err.response?.data || err.message);
    error.value =
      err.response?.data?.message || "Login failed. Please try again.";
  }
};
</script>

<template>
  <div class="container d-flex justify-content-center align-items-center min-vh-100">
    <div class="row w-100">
      <!-- Left side - Company Logo -->
      <div class="col-md-6 d-flex justify-content-center align-items-center py-5">
        <div class="text-center">
          <img src="../assets/bgdcl.jpeg" alt="Company Logo" class="w-40 mb-4" />
          <h1 class="display-4">Bakhrabad Gas Distribution Company Limited</h1>
          <p class="lead text-muted">Customer File Tracking System</p>
        </div>
      </div>

      <!-- Right side - Login Form -->
      <div class="col-md-6 bg-light rounded p-5">
        <h2 class="text-center mb-4">Account Login</h2>

        <form @submit="submitForm">
          <!-- Username Input -->
          <div class="mb-3">
            <label for="username" class="form-label">Username</label>
            <input
              type="text"
              v-model="credentials.username"
              id="username"
              class="form-control"
              placeholder="Enter your username"
              required
            />
          </div>

          <!-- Password Input -->
          <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input
              type="password"
              v-model="credentials.password"
              id="password"
              class="form-control"
              placeholder="Enter your password"
              required
            />
          </div>

          <!-- Error message -->
          <div class="mb-3" v-if="error">
            <div class="alert alert-danger" role="alert">
              {{ error }}
            </div>
          </div>

          <!-- Remember me & Forgot Password -->
          <div class="mb-3 form-check">
            <input type="checkbox" class="form-check-input" id="remember" />
            <label class="form-check-label" for="remember">Remember me</label>
            <a href="#" class="float-end text-decoration-none">Forgot password?</a>
          </div>

          <!-- Login Button -->
          <button type="submit" class="btn btn-primary w-100">Login</button>

          <!-- Reset Password Link -->
          <div class="mt-3 text-center">
            <a href="#" class="text-muted">Reset password</a>
          </div>

          <!-- Register Account -->
          <div class="mt-3 text-center">
            <small class="text-muted">
              Don't have an account?
              <a href="#" class="text-primary">Register now</a>
            </small>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.min-vh-100 {
  min-height: 100vh;
}
</style>
