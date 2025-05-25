import axios from 'axios';
import { useAuthStore } from '@/stores/authStore.js'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_SERVER_URL,
  withCredentials: true,
});

api.interceptors.request.use((config) => {
  const authStore = useAuthStore();
  const token = authStore.accessToken;
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use((response) => {
  //응답을 받았는데 액세스 토큰이 있다면(액세스 토큰이 리프레시 토큰에 의해 재발급된 경우)
  if (response.data.status === 'accessToken') {
    //authStore에 토큰을 다시 저장하고
    const authStore = useAuthStore();
    authStore.accessToken = response.data.accessToken;
    authStore.name = response.data.name;

    //이전의 요청을 다시 시도
    const config = response.config;
    config.headers.Authorization = `Bearer ${authStore.accessToken}`;
    return api.request(config);
  }
  return response;
})

export default api;
