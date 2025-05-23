import { defineStore } from 'pinia'

export const useJwtStore = defineStore('jwtStore', {
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
