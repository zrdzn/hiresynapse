import React, {useEffect, useRef} from 'react'
import PropTypes from 'prop-types'

import {CCol, CRow, CWidgetStatsA} from '@coreui/react'
import {getStyle} from '@coreui/utils'
import {CChartLine} from '@coreui/react-chartjs'
import CIcon from '@coreui/icons-react'
import {cilArrowBottom, cilArrowTop} from '@coreui/icons'

const Widgets = ({ stats, className }) => {
  const widgetChartRef1 = useRef(null)

  const candidatesMonthlyData = Object.values(stats.candidatesFromLastSixMonths.monthlyData)
  const jobsMonthlyData = Object.values(stats.jobsFromLastSixMonths.monthlyData)
  const interviewsMonthlyData = Object.values(stats.interviewsFromLastSixMonths.monthlyData)
  const usersMonthlyData = Object.values(stats.usersFromLastSixMonths.monthlyData)

  const candidatesLabels = Object.keys(stats.candidatesFromLastSixMonths.monthlyData)
  const jobsLabels = Object.keys(stats.jobsFromLastSixMonths.monthlyData)
  const interviewsLabels = Object.keys(stats.interviewsFromLastSixMonths.monthlyData)
  const usersLabels = Object.keys(stats.usersFromLastSixMonths.monthlyData)

  const candidatesGrowthRate = stats.candidatesFromLastSixMonths.growthRate
  const jobsGrowthRate = stats.jobsFromLastSixMonths.growthRate
  const interviewsGrowthRate = stats.interviewsFromLastSixMonths.growthRate
  const usersGrowthRate = stats.usersFromLastSixMonths.growthRate

  useEffect(() => {
    document.documentElement.addEventListener('ColorSchemeChange', () => {
      if (widgetChartRef1.current) {
        setTimeout(() => {
          widgetChartRef1.current.data.datasets[0].pointBackgroundColor = getStyle('--cui-primary')
          widgetChartRef1.current.update()
        })
      }
    })
  }, [widgetChartRef1])

  return (
    <CRow className={className} xs={{ gutter: 4 }}>
      <CCol sm={6} xl={3}>
        <CWidgetStatsA
          color="success"
          value={
            <>
              {candidatesMonthlyData[candidatesMonthlyData.length - 1]}{' '}
              <span className="fs-6 fw-normal">
                ({candidatesGrowthRate}% {candidatesGrowthRate >= 0 ? <CIcon icon={cilArrowTop} /> : <CIcon icon={cilArrowBottom} />})
              </span>
            </>
          }
          title="Candidates applied (Last 6 months)"
          chart={
            <CChartLine
              ref={widgetChartRef1}
              className="mt-3"
              style={{ height: '70px' }}
              data={{
                labels: candidatesLabels,
                datasets: [
                  {
                    label: 'Candidates',
                    backgroundColor: 'rgba(255,255,255,.2)',
                    borderColor: 'rgba(255,255,255,.55)',
                    data: candidatesMonthlyData,
                    fill: true,
                  },
                ],
              }}
              options={{
                plugins: {
                  legend: {
                    display: false,
                  },
                },
                maintainAspectRatio: false,
                scales: {
                  x: {
                    display: false,
                  },
                  y: {
                    display: false,
                  },
                },
                elements: {
                  line: {
                    borderWidth: 2,
                    tension: 0.4,
                  },
                  point: {
                    radius: 0,
                    hitRadius: 10,
                    hoverRadius: 4,
                  },
                },
              }}
            />
          }
        />
      </CCol>
      <CCol sm={6} xl={3}>
        <CWidgetStatsA
          color="info"
          value={
            <>
              {jobsMonthlyData[jobsMonthlyData.length - 1]}{' '}
              <span className="fs-6 fw-normal">
                ({jobsGrowthRate}% {jobsGrowthRate >= 0 ? <CIcon icon={cilArrowTop} /> : <CIcon icon={cilArrowBottom} />})
              </span>
            </>
          }
          title="Jobs created (Last 6 months)"
          chart={
            <CChartLine
              className="mt-3"
              style={{ height: '70px' }}
              data={{
                labels: jobsLabels,
                datasets: [
                  {
                    label: 'Jobs',
                    backgroundColor: 'rgba(255,255,255,.2)',
                    borderColor: 'rgba(255,255,255,.55)',
                    data: jobsMonthlyData,
                    fill: true,
                  },
                ],
              }}
              options={{
                plugins: {
                  legend: {
                    display: false,
                  },
                },
                maintainAspectRatio: false,
                scales: {
                  x: {
                    display: false,
                  },
                  y: {
                    display: false,
                  },
                },
                elements: {
                  line: {
                    borderWidth: 2,
                    tension: 0.4,
                  },
                  point: {
                    radius: 0,
                    hitRadius: 10,
                    hoverRadius: 4,
                  },
                },
              }}
            />
          }
        />
      </CCol>
      <CCol sm={6} xl={3}>
        <CWidgetStatsA
          color="warning"
          value={
            <>
              {interviewsMonthlyData[interviewsMonthlyData.length - 1]}{' '}
              <span className="fs-6 fw-normal">
                ({interviewsGrowthRate}% {interviewsGrowthRate >= 0 ? <CIcon icon={cilArrowTop} /> : <CIcon icon={cilArrowBottom} />})
              </span>
            </>
          }
          title="Interviews conducted (Last 6 months)"
          chart={
            <CChartLine
              className="mt-3"
              style={{ height: '70px' }}
              data={{
                labels: interviewsLabels,
                datasets: [
                  {
                    label: 'Interviews',
                    backgroundColor: 'rgba(255,255,255,.2)',
                    borderColor: 'rgba(255,255,255,.55)',
                    data: interviewsMonthlyData,
                    fill: true,
                  },
                ],
              }}
              options={{
                plugins: {
                  legend: {
                    display: false,
                  },
                },
                maintainAspectRatio: false,
                scales: {
                  x: {
                    display: false,
                  },
                  y: {
                    display: false,
                  },
                },
                elements: {
                  line: {
                    borderWidth: 2,
                    tension: 0.4,
                  },
                  point: {
                    radius: 0,
                    hitRadius: 10,
                    hoverRadius: 4,
                  },
                },
              }}
            />
          }
        />
      </CCol>
      <CCol sm={6} xl={3}>
        <CWidgetStatsA
          color="primary"
          value={
            <>
              {usersMonthlyData[usersMonthlyData.length - 1]}{' '}
              <span className="fs-6 fw-normal">
                ({usersGrowthRate}% {usersGrowthRate >= 0 ? <CIcon icon={cilArrowTop} /> : <CIcon icon={cilArrowBottom} />})
              </span>
            </>
          }
          title="Staff joined (Last 6 months)"
          chart={
            <CChartLine
              className="mt-3"
              style={{ height: '70px' }}
              data={{
                labels: usersLabels,
                datasets: [
                  {
                    label: 'Staff',
                    backgroundColor: 'rgba(255,255,255,.2)',
                    borderColor: 'rgba(255,255,255,.55)',
                    data: usersMonthlyData,
                    fill: true,
                  },
                ],
              }}
              options={{
                plugins: {
                  legend: {
                    display: false,
                  },
                },
                maintainAspectRatio: false,
                scales: {
                  x: {
                    display: false,
                  },
                  y: {
                    display: false,
                  },
                },
                elements: {
                  line: {
                    borderWidth: 2,
                    tension: 0.4,
                  },
                  point: {
                    radius: 0,
                    hitRadius: 10,
                    hoverRadius: 4,
                  },
                },
              }}
            />
          }
        />
      </CCol>
    </CRow>
  )
}

Widgets.propTypes = {
  stats: PropTypes.object.isRequired,
  className: PropTypes.string,
}

export default Widgets
