import axios, { type AxiosInstance } from "axios";

// Create a typed Axios instance
const api: AxiosInstance = axios.create({
	baseURL: "http://localhost:8080/api" // your Spring Boot backend
});

export default api;
