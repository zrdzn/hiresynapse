import api from './api'

const TASKS_URL = '/tasks'

export const taskService = {
  getTasks: () => {
    return api.get(`${TASKS_URL}`)
  },
}
