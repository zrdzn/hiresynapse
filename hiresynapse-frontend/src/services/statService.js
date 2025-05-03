import api from './api'

const STATS_URL = '/stats'

export const statService = {
  getStats: () => api.get(STATS_URL),
}
