import React from 'react'
import { CFooter } from '@coreui/react'

const AppFooter = () => {
  return (
    <CFooter className="px-4">
      <div>
        <a href="https://github.com/zrdzn/hiresynapse" target="_blank" rel="noopener noreferrer">
          HireSynapse
        </a>
        <span className="ms-1">v1.0.0</span>
      </div>
    </CFooter>
  )
}

export default React.memo(AppFooter)
