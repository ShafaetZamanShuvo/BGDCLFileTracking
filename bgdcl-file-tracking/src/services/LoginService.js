import axios from "axios";

// Login function that returns the response data from the backend after a successful login
const login = async (username, password) => {
  try {
    const response = await axios.post("http://localhost:8081/api/auth/signin", {
      username,
      password,
    });
    return response.data; // returns the response, including accessToken and user info
  } catch (error) {
    throw new Error(error.response?.data?.message || "Login failed.");
  }
};

// Fetch user information using the access token
const getUserInfo = async (token) => {
  try {
    const response = await axios.get("http://localhost:8081/api/auth/user-info", {
      headers: {
        Authorization: `Bearer ${token}`, // Use the token for authorization
      },
    });
    return response.data; // Returns user information
  } catch (error) {
    throw new Error("Error fetching user information. Please try again.");
  }
};

export { login, getUserInfo };

