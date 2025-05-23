import { defineStore } from 'pinia'

export const useAuthStore = defineStore('authStore', {
  state: () => ({
    accessToken: null,
    name: null,
  }),

  getters: {
    isLoggedIn: (state) => !!state.accessToken,
  },

  actions: {
    setAuthInfo(token, name) {
      this.accessToken = token
      this.name = name
    },

    clearAuthInfo() {
      this.accessToken = null
      this.name = null
    },
  },

  persist: true,
})
