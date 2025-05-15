import React from 'react';
import {CButton, CCol, CForm, CFormCheck, CFormInput, CFormTextarea, CRow} from '@coreui/react';

export const JobForm =
  ({
     jobData,
     onChange,
     onSubmit,
     isEdit = false,
     jobStatus,
     setJobStatus,
     titleError,
     descriptionError,
     locationError,
   }) => {
  return (
    <CForm onChange={onChange}>
      <CRow className="mb-3">
        <CCol md={6}>
          <CFormInput
            name="title"
            type="text"
            label="Title"
            placeholder="Software Engineer..."
            text="Must be 3-50 characters long"
            required
            value={jobData.title}
            valid={jobData.title !== '' && !titleError}
            invalid={titleError}
          />
          <div className="mt-3">
            <CFormTextarea
              name="description"
              type="text"
              label="Description"
              placeholder="Job description"
              rows={3}
              text="Must be 3-1000 characters long"
              required
              value={jobData.description}
              valid={jobData.description !== '' && !descriptionError}
              invalid={descriptionError}
            ></CFormTextarea>
          </div>
          <div className="mt-3">
            <CFormInput
              name="location"
              type="text"
              label="Location"
              placeholder="Amsterdam / Warsaw / Remote..."
              text="Must not be empty"
              required
              value={jobData.location}
              valid={jobData.location !== '' && !locationError}
              invalid={locationError}
            />
          </div>
        </CCol>

        <CCol md={6}>
          <CFormInput
            name="requirements"
            type="text"
            label="Requirements"
            value={jobData.requirements}
            placeholder="Python, Java, C++..."
          />
          <div className="mt-3">
            <CFormInput
              name="benefits"
              type="text"
              label="Benefits"
              value={jobData.benefits}
              placeholder="Free lunch, Gym membership..."
            />
          </div>
          <div className="mt-3">
            <CFormInput
              name="salary"
              type="text"
              label="Salary range"
              value={jobData.salary}
              placeholder="$2000 - $3000"
            />
          </div>
          <div className="mt-3">
            <CFormInput
              name="requiredExperience"
              type="text"
              label="Required experience"
              value={jobData.requiredExperience}
              placeholder="2 years"
            />
          </div>
          {!isEdit && (
            <div className="mt-3">
              <CFormCheck
                id="check1"
                label="Auto publish job after creation"
                defaultChecked={jobStatus === 'PUBLISHED'}
                onChange={(event) =>
                  event.target.checked === true
                    ? setJobStatus('PUBLISHED')
                    : setJobStatus('UNPUBLISHED')
                }
              />
            </div>
          )}
        </CCol>
      </CRow>

      <CButton color="primary" className="mt-3" onClick={onSubmit}>
        {isEdit ? 'Save Changes' : 'Create Job'}
      </CButton>
    </CForm>
  )
}
