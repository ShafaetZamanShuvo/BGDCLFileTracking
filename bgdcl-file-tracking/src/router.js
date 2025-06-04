import { createRouter, createWebHistory } from 'vue-router';
import Homepage from './pages/Homepage.vue';
import Login from './components/Login.vue';
import PendingFiles from './components/PendingFiles.vue';
import RequestedFiles from './components/RequestedFiles.vue';

const routes = [
    {
        path: "/",
        redirect: "/login",  // Redirect root path to login page
    },
    {
        path: "/home",
        name: "Homepage", 
        component: Homepage,
        meta: { requiresAuth: true },  // Add meta field to require auth
    },
    {
        path: "/login",
        name: "Login",
        component: Login,
    },
    {
        path: "/accept-files",
        name: "PendingFiles",
        component: PendingFiles,
        meta: { requiresAuth: true },  // Add meta field to require auth
    },
    {
        path: "/requested-files",
        name: "RequestedFiles",
        component: RequestedFiles,
        meta: { requiresAuth: true },  // Add meta field to require auth
    },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Navigation guard to check authentication
router.beforeEach((to, from, next) => {
    const isAuthenticated = localStorage.getItem('authToken');
    if (to.matched.some(record => record.meta.requiresAuth) && !isAuthenticated) {
        next('/login'); // Redirect to login if not authenticated
    } else {
        next(); // Proceed to the route
    }
});

export default router;

