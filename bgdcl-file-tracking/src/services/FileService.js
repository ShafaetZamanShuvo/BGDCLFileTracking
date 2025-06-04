import axios from "axios"
import { API_URL } from "../config"

export default {

  

  async getFileStatus(fileCode) {
    const token = localStorage.getItem("authToken");
    try {
      const response = await axios.get(`http://localhost:8081/api/files/${fileCode}/status`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
      
      return response.data;
    } catch (error) {
      console.error("Error fetching file status:", error)
      throw error
    }
  },

  async getFilesByDepartment({ departmentId, page, size, sort, direction }) {
    const token = localStorage.getItem('authToken');
    try {
      const response = await axios.get(
        `${API_URL}/api/files/department/${departmentId}`,
        {
          params: {
            page,
            size,
            sort,
            direction
          },
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );
      return {
        files: response.data.files || response.data.content, // adapt as per your API shape
        totalPages: response.data.totalPages
      };
    } catch (error) {
      console.error('Error fetching department files:', error);
      throw error;
    }
  },

  async transferFile(payload) {
    const token = localStorage.getItem("authToken");
    try {
      const response = await axios.post(`${API_URL}/api/files/transfer`, payload, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      return response.data;
    } catch (error) {
      console.error("Error transferring file:", error);
      throw error;
    }
  },

  async takeBackFile(payload) {
    const token = localStorage.getItem("authToken");
    try {
      const response = await axios.post(`${API_URL}/api/files/return`, payload, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      return response.data;
    } catch (error) {
      console.error("Error returning file:", error);
      throw error;
    }
  },

  async getTransactionHistory(fileCode) {
    const token = localStorage.getItem("authToken");
    try {
      const response = await axios.get(`${API_URL}/api/files/${fileCode}/history`,  {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      return response.data;
    } catch (error) {
      console.error("Error transferring file:", error);
      throw error;
    }
  },

  async acceptFile(payload) {
    const token = localStorage.getItem("authToken");
    try {
      const response = await axios.post(`${API_URL}/api/files/accept`, payload, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      return response.data;
    } catch (error) {
      console.error("Error accepting file:", error);
      throw error;
    }
  },

  async requestFile(payload) {
    const token = localStorage.getItem("authToken");
    try {
      const response = await axios.post(`${API_URL}/api/files/request`, payload, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      return response.data;
    } catch (error) {
      console.error("Error requesting file:", error); 
      throw error;
    }
  },

  async getToAcceptFilesList(departmentId) {
    const token = localStorage.getItem("authToken");
    try {
      const response = await axios.get(`${API_URL}/api/files/pending/${departmentId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      return response.data;
    } catch (error) {
      console.error("Error getting to accept file:", error);
      throw error;
    }
  },

  async getRequestedFilesList(departmentId) {
    const token = localStorage.getItem("authToken");
    try {
      const response = await axios.get(`${API_URL}/api/files/requests/${departmentId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      return response.data;
    } catch (error) {
      console.error("Error getting to accept file:", error);
      throw error;
    }
  },

  async getRequestedFilesListDetails(departmentId) {
    const token = localStorage.getItem("authToken");
    try {
      const response = await axios.get(`${API_URL}/api/files/requests/${departmentId}/details`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      return response.data;
    } catch (error) {
      console.error("Error getting to accept file:", error);
      throw error;
    }
  },
  


}