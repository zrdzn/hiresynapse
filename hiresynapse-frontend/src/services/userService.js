import api from './api'

const USERS_URL = '/users'

export const userService = {
  getUsers: () => api.get(USERS_URL),
  deleteUser: (userId) => {
    if (!userId) {
      throw new Error('User ID is required')
    }
    return api.delete(`${USERS_URL}/${userId}`)
  },
}
