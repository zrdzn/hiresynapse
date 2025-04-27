import React, {useEffect, useState} from "react";
import {CFormInput, CListGroup, CListGroupItem, CSpinner} from "@coreui/react";
import {toast} from "react-toastify";
import {candidateService} from "../../services/candidateService";

export const CandidateSearch = (props) => {
  const [searchTerm, setSearchTerm] = useState("")
  const [showResults, setShowResults] = useState(false)
  const [candidates, setCandidates] = useState([])
  const [loading, setLoading] = useState(true);

  const filteredCandidates = candidates.filter(
    (candidate) =>
      candidate.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      candidate.lastName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      candidate.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
      candidate.phone.toLowerCase().includes(searchTerm.toLowerCase()) ||
      candidate.job.title.toLowerCase().includes(searchTerm.toLowerCase()),
  )

  useEffect(() => {
    candidateService.getPendingCandidates()
      .then(response => {
        setCandidates(response.data)
        setLoading(false)
      })
      .catch(err => {
        console.error(err)
        if (Array.isArray(err.response.data)) {
          err.response.data.forEach(message => toast.error(message))
        }
      })
  }, []);

  useEffect(() => {
    setShowResults(searchTerm.length > 0)
  }, [searchTerm])

  if (loading) {
    return (
      <div className="pt-3 text-center">
        <CSpinner />
      </div>
    );
  }

  return (
    <div className="candidate-search position-relative">
      <label className="form-label">Select candidate</label>
      <CFormInput
        type="text"
        placeholder="Search candidates..."
        value={props.selectedCandidate ? `${props.selectedCandidate.firstName} ${props.selectedCandidate.lastName}` : searchTerm}
        onChange={event => setSearchTerm(event.target.value)}
        onFocus={() => setShowResults(true)}
      />

      {showResults && (
        <CListGroup className="position-absolute w-100 shadow-sm" style={{ zIndex: 1000 }}>
          {filteredCandidates.length > 0 ? (
            filteredCandidates.map((candidate) => (
              <CListGroupItem
                key={candidate.id}
                className="cursor-pointer"
                onClick={() => {
                  props.onSelectCandidate(candidate)
                  setSearchTerm("")
                  setShowResults(false)
                }}
              >
                <div className="d-flex justify-content-between align-items-center">
                  <div>
                    <div className="fw-bold">{candidate.firstName} {candidate.lastName}</div>
                    <div className="small text-muted">{candidate.email} | {candidate.phone}</div>
                    <div className="small text-muted">{candidate.job.title}</div>
                  </div>
                </div>
              </CListGroupItem>
            ))
          ) : (
            <CListGroupItem>No candidates found</CListGroupItem>
          )}
        </CListGroup>
      )}
    </div>
  )
}
