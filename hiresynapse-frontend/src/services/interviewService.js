import api from './api'

const INTERVIEWS_URL = '/interviews'

export const interviewService = {
  createInterview: (request) => {
    if (!request) {
      throw new Error('Request is required')
    }
    return api.post(`${INTERVIEWS_URL}`, request)
  },
  getInterviews: () => {
    return api.get(`${INTERVIEWS_URL}`)
  },
  getConfirmedInterviews: () => {
    return api.get(`${INTERVIEWS_URL}/confirmed?size=3`)
  },
  getUnconfirmedInterviews: () => {
    return api.get(`${INTERVIEWS_URL}/unconfirmed?size=3`)
  },
}
