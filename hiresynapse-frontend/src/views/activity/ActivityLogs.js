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
                      <CTableDataCell>
                        <div>{capitalize(item.action)}</div>
                      </CTableDataCell>
                      <CTableDataCell>
                        <div>{item.description}</div>
                      </CTableDataCell>
                      <CTableDataCell>
                        <div>[{item.entityId}] {capitalize(item.entityType)}</div>
                      </CTableDataCell>
                      <CTableDataCell>
                        {
                          item.performer.id === null ?
                            <div>System</div> :
                            <div>[{item.performer.id}] {item.performer.firstName} {item.performer.lastName}</div>
                        }
                      </CTableDataCell>
                      <CTableDataCell className="text-end">
                        <div>{formatDate(item.loggedAt, true)}</div>
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
