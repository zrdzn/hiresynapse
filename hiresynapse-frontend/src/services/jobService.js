import api from './api'

const JOBS_URL = '/jobs'

export const jobService = {
  createJob: (request) => {
    if (!request) {
      throw new Error('Request is required')
    }
    return api.post(`${JOBS_URL}`, request)
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
  getJobs: () => {
    return api.get(`${JOBS_URL}`)
  },
  getJob: (jobId) => {
    if (!jobId) {
      throw new Error('Job ID is required')
    }
    return api.get(`${JOBS_URL}/${jobId}`)
  },
  getPublishedJobs: () => {
    return api.get(`${JOBS_URL}/published`)
  },
  publishJob: (jobId) => {
    if (!jobId) {
      throw new Error('Job ID is required')
    }
    return api.patch(`${JOBS_URL}/${jobId}/publish`)
  },
  unpublishJob: (jobId) => {
    if (!jobId) {
      throw new Error('Job ID is required')
    }
    return api.patch(`${JOBS_URL}/${jobId}/unpublish`)
  },
  scheduleJob: (jobId, publishAt) => {
    if (!jobId) {
      throw new Error('Job ID is required')
    }

    if (!publishAt) {
      throw new Error('Job publish date is required')
    }

    return api.patch(`${JOBS_URL}/${jobId}/schedule/${publishAt}`)
  },
  deleteJob: (jobId) => {
    if (!jobId) {
      throw new Error('Job ID is required')
    }
    return api.delete(`${JOBS_URL}/${jobId}`)
  },
}
