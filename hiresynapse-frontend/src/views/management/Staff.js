import React, {useEffect, useState} from 'react'
import {
  CButton,
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
import {capitalize} from "../../hooks/wordCapitalizeUtil";
import {userService} from "../../services/userService";
import {toast} from "react-toastify";

const Staff = () => {
  const [staff, setStaff] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    userService.getUsers()
      .then(response => {
        setStaff(response.data);
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

  const handleStaffDelete = (event, id) => {
    event.preventDefault()

    userService.deleteUser(id)
      .then(() => {
        setStaff([...staff.filter(user => user.id !== id)]);
        toast.success('User removed successfully')
      })
      .catch(error => {
        console.error(error)
        toast.error('Could not remove user')
      })
  }

  return (
    <>
      <CRow>
        <CCol xs>
          <CCard className="mb-4">
            <CCardHeader>
              <span>Manage staff</span>
            </CCardHeader>
            <CCardBody>
              <CTable align="middle" className="mb-0 border" hover responsive>
                <CTableHead className="text-nowrap">
                  <CTableRow>
                    <CTableHeaderCell className="bg-body-tertiary">Username</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary">Email</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary">Name</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary">Role</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary text-end">Actions</CTableHeaderCell>
                  </CTableRow>
                </CTableHead>
                <CTableBody>
                  {staff.map((item, index) => (
                    <CTableRow v-for="item in tableItems" key={index}>
                      <CTableDataCell className="fw-semibold">
                        <div>{item.username}</div>
                      </CTableDataCell>
                      <CTableDataCell className="fw-semibold">
                        <div>{item.email}</div>
                      </CTableDataCell>
                      <CTableDataCell>
                        <div>{item.firstName} {item.lastName}</div>
                      </CTableDataCell>
                      <CTableDataCell className="fw-semibold">
                        <div>{capitalize(item.role)}</div>
                      </CTableDataCell>
                      <CTableDataCell className="text-end">
                        <CButton
                          onClick={event => handleStaffDelete(event, item.id)}
                          className="text-white bg-danger"
                          size="sm">
                          Remove
                        </CButton>
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

export default Staff
