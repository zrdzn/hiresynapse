import api from './api'

const CANDIDATES_URL = '/candidates'

export const candidateService = {
  createCandidate: (request, resumeFile) => {
    if (!request) {
      throw new Error('Request is required')
    }

    if (!resumeFile) {
      throw new Error('Resume file is required')
    }

    const form = new FormData()
    form.append("dto", new Blob([JSON.stringify(request)], {
      type: 'application/json'
    }))
    form.append("file", resumeFile)

    return api.post(`${CANDIDATES_URL}`, form, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    })
  },
  getCandidates: () => api.get(CANDIDATES_URL),
  getPendingCandidates: () => api.get(`${CANDIDATES_URL}/pending`),
  getCandidate: (candidateId) => {
    if (!candidateId) {
      throw new Error('Candidate ID is required')
    }

    return api.get(`${CANDIDATES_URL}/${candidateId}`)
  },
  acceptCandidate: (candidateId) => {
    if (!candidateId) {
      throw new Error('Candidate ID is required')
    }

    return api.patch(`${CANDIDATES_URL}/${candidateId}/accept`)
  },
  rejectCandidate: (candidateId) => {
    if (!candidateId) {
      throw new Error('Candidate ID is required')
    }

    return api.patch(`${CANDIDATES_URL}/${candidateId}/reject`)
  },
}
