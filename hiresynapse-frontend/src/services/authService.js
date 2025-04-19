import api from './api'

const AUTH_URL = '/auth'

export const authService = {
  getAuthDetails: () => api.get(`${AUTH_URL}/me`),
}
