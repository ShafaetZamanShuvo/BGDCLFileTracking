import { createApp } from 'vue';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import 'bootstrap';
import App from './App.vue';
import router from './router';

import Toast from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

const app = createApp(App);

app.use(router);
app.use(Toast, {
  autoClose: 3000,           // Optional: auto close after 3 seconds
  position: "top-right",     // Optional: position of toast
  pauseOnHover: true,
  theme: "light",            // Options: "light" | "dark" | "colored"
});

app.mount('#app');


