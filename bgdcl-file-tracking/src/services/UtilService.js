import axios from "axios";
import { API_URL } from "../config";

export default {
  async getAllUsers() {
    const token = localStorage.getItem("authToken");
    try {
      const response = await axios.get(`${API_URL}/api/user/get-all-users`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      return response.data;
    } catch (error) {
      console.error("Error fetching users:", error);
      throw error;
    }
  },
  async getAllDepartments() {
    const token = localStorage.getItem("authToken");
    try {
      const response = await axios.get(`${API_URL}/api/department/get-all-departments`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      return response.data;
    } catch (error) {
      console.error("Error fetching departments:", error);
      throw error;
    }
  },
  async getAllSections() {
    const token = localStorage.getItem("authToken");
    try {
      const response = await axios.get(`${API_URL}/api/section/get-all-sections`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      return response.data;
    } catch (error) {
      console.error("Error fetching sections:", error);
      throw error;
    }
  },


};
