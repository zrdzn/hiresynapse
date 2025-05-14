import React, {useEffect, useState} from 'react'
import {
  CCard,
  CCardBody,
  CCardHeader,
  CCol,
  CRow,
  CSpinner,
  CTable,
  CTableBody,
  CTableDataCell,
  CTableHead,
  CTableHeaderCell,
  CTableRow
} from "@coreui/react";
import {logService} from "../../services/logService";
import {capitalize} from "../../hooks/wordCapitalizeUtil";
import {useDateFormatter} from "../../hooks/useDateFormatter";

const ActivityLogs = () => {
  const { formatDate } = useDateFormatter()
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    logService.getLogs()
      .then(response => {
        setLogs(response.data);
        setLoading(false);
      })
      .catch(error => console.error(error))
  }, []);

  if (loading) {
    return (
      <div className="pt-3 text-center">
        <CSpinner />
      </div>
    );
  }

  return (
    <>
      <CRow>
        <CCol xs>
          <CCard className="mb-4">
            <CCardHeader>
              <span>Activity logs</span>
            </CCardHeader>
            <CCardBody>
              <CRow>
                <CCol xs={12} md={6} xl={6}>
                  <CRow>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-info py-1 px-3">
                        <div className="text-body-secondary text-truncate small">All</div>
                        <div className="fs-5 fw-semibold">{logs.length}</div>
                      </div>
                    </CCol>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-danger py-1 px-3 mb-3">
                        <div className="text-body-secondary text-truncate small">Delete</div>
                        <div className="fs-5 fw-semibold">{logs.filter(log => log.action === 'DELETE').length}</div>
                      </div>
                    </CCol>
                  </CRow>
                </CCol>
                <CCol xs={12} md={6} xl={6}>
                  <CRow>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-warning py-1 px-3 mb-3">
                        <div className="text-body-secondary text-truncate small">Update</div>
                        <div className="fs-5 fw-semibold">{logs.filter(log => log.action === 'UPDATE').length}</div>
                      </div>
                    </CCol>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-success py-1 px-3 mb-3">
                        <div className="text-body-secondary text-truncate small">Create</div>
                        <div className="fs-5 fw-semibold">{logs.filter(log => log.action === 'CREATE').length}</div>
                      </div>
                    </CCol>
                  </CRow>
                </CCol>
              </CRow>
              <CTable align="middle" className="mb-0 border" hover responsive>
                <CTableHead className="text-nowrap">
                  <CTableRow>
                    <CTableHeaderCell className="bg-body-tertiary">Action</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary">Description</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary">Entity</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary">Performer</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary text-end">Logged at</CTableHeaderCell>
                  </CTableRow>
                </CTableHead>
                <CTableBody>
                  {logs.map((item, index) => (
                    <CTableRow v-for="item in tableItems" key={index}>
                      <CTableDataCell className="fw-semibold">
                        <div>{capitalize(item.action)}</div>
                      </CTableDataCell>
                      <CTableDataCell>
                        <div>{item.description}</div>
                      </CTableDataCell>
                      <CTableDataCell className="fw-semibold">
                        <div>{capitalize(item.entityType)}</div>
                      </CTableDataCell>
                      <CTableDataCell className="fw-semibold">
                        {
                          item.performerId === null ?
                            <div>{item.performerName}</div> :
                            <div>{item.performerName}</div>
                        }
                      </CTableDataCell>
                      <CTableDataCell className="text-end">
                        <div>{formatDate(item.createdAt, true)}</div>
                      </CTableDataCell>
                    </CTableRow>
                  ))}
                </CTableBody>
              </CTable>
            </CCardBody>
          </CCard>
        </CCol>
      </CRow>
    </>
  )
}

export default ActivityLogs
