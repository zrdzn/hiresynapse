import api from './api'

const STATS_URL = '/stats'

export const statisticService = {
  getStats: () => api.get(STATS_URL),
}
