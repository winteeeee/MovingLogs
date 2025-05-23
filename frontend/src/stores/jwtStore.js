import { defineStore } from 'pinia'

export const jwtStore = defineStore('auth', {
  state: () => ({
    accessToken: null,
  }),

  getters: {
    isLoggedIn: (state) => !!state.accessToken,
  },

  actions: {
    setAccessToken(token) {
      this.accessToken = token
    },

    clearAccessToken() {
      this.accessToken = null
    },
  },

  persist: true,
})
