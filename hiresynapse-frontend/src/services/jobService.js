import api from './api'

const JOBS_URL = '/jobs'

export const jobService = {
  createJob: (request) => {
    if (!request) {
      throw new Error('Request is required')
    }
    return api.post(`${JOBS_URL}`, request)
  },
  getJobs: () => {
    return api.get(`${JOBS_URL}`)
  },
  getPublishedJobs: () => {
    return api.get(`${JOBS_URL}/published`)
  },
  getJobById: (jobId) => {
    if (!jobId) {
      throw new Error('Job ID is required')
    }
    return api.get(`${JOBS_URL}/${jobId}`)
  },
  updateJob: (jobId, request) => {
    if (!jobId) {
      throw new Error('Job ID is required')
    }
    if (!request) {
      throw new Error('Request is required')
    }
    return api.patch(`${JOBS_URL}/${jobId}`, request)
  },
  deleteJob: (jobId) => {
    if (!jobId) {
      throw new Error('Job ID is required')
    }
    return api.delete(`${JOBS_URL}/${jobId}`)
  },
}
