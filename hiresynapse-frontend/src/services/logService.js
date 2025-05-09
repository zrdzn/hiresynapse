import api from './api'

const LOGS_URL = '/logs'

export const logService = {
  getLogs: () => api.get(LOGS_URL)
}
