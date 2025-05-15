import React from 'react'
import {CAvatar, CDropdown, CDropdownItem, CDropdownMenu, CDropdownToggle,} from '@coreui/react'
import {cilAccountLogout,} from '@coreui/icons'
import CIcon from '@coreui/icons-react'

const AppHeaderDropdown = ({ authDetails }) => {
  return (
    <CDropdown variant="nav-item">
      <CDropdownToggle placement="bottom-end" className="py-0 pe-0" caret={false}>
        <CAvatar src={authDetails.pictureUrl ?? authDetails.username} size="md" />
      </CDropdownToggle>
      <CDropdownMenu className="pt-0" placement="bottom-end">
        <CDropdownItem href="http://localhost:8080/logout">
          <CIcon icon={cilAccountLogout} className="me-2" />
          Logout
        </CDropdownItem>
      </CDropdownMenu>
    </CDropdown>
  )
}

export default AppHeaderDropdown
