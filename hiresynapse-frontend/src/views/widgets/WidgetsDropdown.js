import React, {useEffect, useRef} from 'react'
import PropTypes from 'prop-types'

import {CCol, CDropdown, CDropdownItem, CDropdownMenu, CDropdownToggle, CRow, CWidgetStatsA,} from '@coreui/react'
import {getStyle} from '@coreui/utils'
import {CChartLine} from '@coreui/react-chartjs'
import CIcon from '@coreui/icons-react'
import {cilArrowBottom, cilArrowTop, cilOptions} from '@coreui/icons'

const WidgetsDropdown = (props) => {
  const widgetChartRef1 = useRef(null)
  const widgetChartRef2 = useRef(null)
  const widgetChartRef3 = useRef(null)
  const widgetChartRef4 = useRef(null)

  const aiAnalysesData = [30, 42, 50, 65, 78, 92]
  const interviewsData = [22, 35, 40, 48, 55, 60]

  const last6Months = getLastSixMonths()

  const aiAnalysesPercentChange = calculatePercentChange(aiAnalysesData)
  const interviewsPercentChange = calculatePercentChange(interviewsData)

  useEffect(() => {
    document.documentElement.addEventListener('ColorSchemeChange', () => {
      if (widgetChartRef1.current) {
        setTimeout(() => {
          widgetChartRef1.current.data.datasets[0].pointBackgroundColor = getStyle('--cui-primary')
          widgetChartRef1.current.update()
        })
      }

      if (widgetChartRef2.current) {
        setTimeout(() => {
          widgetChartRef2.current.data.datasets[0].pointBackgroundColor = getStyle('--cui-info')
          widgetChartRef2.current.update()
        })
      }

      if (widgetChartRef3.current) {
        setTimeout(() => {
          widgetChartRef3.current.data.datasets[0].pointBackgroundColor = getStyle('--cui-warning')
          widgetChartRef3.current.update()
        })
      }

      if (widgetChartRef4.current) {
        setTimeout(() => {
          widgetChartRef4.current.data.datasets[0].pointBackgroundColor = getStyle('--cui-danger')
          widgetChartRef4.current.update()
        })
      }
    })
  }, [widgetChartRef1, widgetChartRef2, widgetChartRef3, widgetChartRef4])

  return (
    <CRow className={props.className} xs={{ gutter: 4 }}>
      <CCol sm={6} xl={3}>
        <CWidgetStatsA
          color="success"
          value={
            <>
              {aiAnalysesData[aiAnalysesData.length - 1]}{' '}
              <span className="fs-6 fw-normal">
                ({aiAnalysesPercentChange}% {aiAnalysesPercentChange >= 0 ? <CIcon icon={cilArrowTop} /> : <CIcon icon={cilArrowBottom} />})
              </span>
            </>
          }
          title="Job Offers"
          action={
            <CDropdown alignment="end">
              <CDropdownToggle color="transparent" caret={false} className="text-white p-0">
                <CIcon icon={cilOptions} />
              </CDropdownToggle>
              <CDropdownMenu>
                <CDropdownItem>View Analysis Results</CDropdownItem>
                <CDropdownItem>Run New Analysis</CDropdownItem>
                <CDropdownItem>AI Settings</CDropdownItem>
              </CDropdownMenu>
            </CDropdown>
          }
          chart={
            <CChartLine
              ref={widgetChartRef3}
              className="mt-3"
              style={{ height: '70px' }}
              data={{
                labels: last6Months,
                datasets: [
                  {
                    label: 'AI Analyses',
                    backgroundColor: 'rgba(255,255,255,.2)',
                    borderColor: 'rgba(255,255,255,.55)',
                    data: aiAnalysesData,
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
              {aiAnalysesData[aiAnalysesData.length - 1]}{' '}
              <span className="fs-6 fw-normal">
                ({aiAnalysesPercentChange}% {aiAnalysesPercentChange >= 0 ? <CIcon icon={cilArrowTop} /> : <CIcon icon={cilArrowBottom} />})
              </span>
            </>
          }
          title="AI Analyses Performed"
          action={
            <CDropdown alignment="end">
              <CDropdownToggle color="transparent" caret={false} className="text-white p-0">
                <CIcon icon={cilOptions} />
              </CDropdownToggle>
              <CDropdownMenu>
                <CDropdownItem>View Analysis Results</CDropdownItem>
                <CDropdownItem>Run New Analysis</CDropdownItem>
                <CDropdownItem>AI Settings</CDropdownItem>
              </CDropdownMenu>
            </CDropdown>
          }
          chart={
            <CChartLine
              ref={widgetChartRef3}
              className="mt-3"
              style={{ height: '70px' }}
              data={{
                labels: last6Months,
                datasets: [
                  {
                    label: 'AI Analyses',
                    backgroundColor: 'rgba(255,255,255,.2)',
                    borderColor: 'rgba(255,255,255,.55)',
                    data: aiAnalysesData,
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
              {aiAnalysesData[aiAnalysesData.length - 1]}{' '}
              <span className="fs-6 fw-normal">
                ({aiAnalysesPercentChange}% {aiAnalysesPercentChange >= 0 ? <CIcon icon={cilArrowTop} /> : <CIcon icon={cilArrowBottom} />})
              </span>
            </>
          }
          title="Candidates Applied"
          action={
            <CDropdown alignment="end">
              <CDropdownToggle color="transparent" caret={false} className="text-white p-0">
                <CIcon icon={cilOptions} />
              </CDropdownToggle>
              <CDropdownMenu>
                <CDropdownItem>View Analysis Results</CDropdownItem>
                <CDropdownItem>Run New Analysis</CDropdownItem>
                <CDropdownItem>AI Settings</CDropdownItem>
              </CDropdownMenu>
            </CDropdown>
          }
          chart={
            <CChartLine
              ref={widgetChartRef3}
              className="mt-3"
              style={{ height: '70px' }}
              data={{
                labels: last6Months,
                datasets: [
                  {
                    label: 'AI Analyses',
                    backgroundColor: 'rgba(255,255,255,.2)',
                    borderColor: 'rgba(255,255,255,.55)',
                    data: aiAnalysesData,
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
          color="danger"
          value={
            <>
              {aiAnalysesData[aiAnalysesData.length - 1]}{' '}
              <span className="fs-6 fw-normal">
                ({aiAnalysesPercentChange}% {aiAnalysesPercentChange >= 0 ? <CIcon icon={cilArrowTop} /> : <CIcon icon={cilArrowBottom} />})
              </span>
            </>
          }
          title="Interviews Conducted"
          action={
            <CDropdown alignment="end">
              <CDropdownToggle color="transparent" caret={false} className="text-white p-0">
                <CIcon icon={cilOptions} />
              </CDropdownToggle>
              <CDropdownMenu>
                <CDropdownItem>View Analysis Results</CDropdownItem>
                <CDropdownItem>Run New Analysis</CDropdownItem>
                <CDropdownItem>AI Settings</CDropdownItem>
              </CDropdownMenu>
            </CDropdown>
          }
          chart={
            <CChartLine
              ref={widgetChartRef3}
              className="mt-3"
              style={{ height: '70px' }}
              data={{
                labels: last6Months,
                datasets: [
                  {
                    label: 'AI Analyses',
                    backgroundColor: 'rgba(255,255,255,.2)',
                    borderColor: 'rgba(255,255,255,.55)',
                    data: aiAnalysesData,
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

function getLastSixMonths() {
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  const today = new Date();
  const labels = [];

  for (let i = 5; i >= 0; i--) {
    const date = new Date();
    date.setMonth(today.getMonth() - i);
    labels.push(months[date.getMonth()]);
  }

  return labels;
}

function calculatePercentChange(data) {
  if (data.length < 2 || data[0] === 0) return 0;

  const firstValue = data[0];
  const lastValue = data[data.length - 1];
  const percentChange = ((lastValue - firstValue) / firstValue) * 100;

  return Math.round(percentChange * 10) / 10;
}

WidgetsDropdown.propTypes = {
  className: PropTypes.string,
  withCharts: PropTypes.bool,
}

export default WidgetsDropdown
