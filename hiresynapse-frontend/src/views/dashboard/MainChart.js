import React, {useEffect, useRef} from 'react'

import {CChartLine} from '@coreui/react-chartjs'
import {getStyle} from '@coreui/utils'

const MainChart = ({ stats }) => {
  const chartRef = useRef(null)

  const acceptedCandidatesMonthlyData = Object.values(stats.acceptedCandidatesFromLastSixMonths.monthlyData)
  const rejectedCandidatesMonthlyData = Object.values(stats.rejectedCandidatesFromLastSixMonths.monthlyData)
  const pendingCandidatesMonthlyData = Object.values(stats.pendingCandidatesFromLastSixMonths.monthlyData)

  const labels = Object.keys(stats.acceptedCandidatesFromLastSixMonths.monthlyData)


  useEffect(() => {
    document.documentElement.addEventListener('ColorSchemeChange', () => {
      if (chartRef.current) {
        setTimeout(() => {
          chartRef.current.options.scales.x.grid.borderColor = getStyle(
            '--cui-border-color-translucent',
          )
          chartRef.current.options.scales.x.grid.color = getStyle('--cui-border-color-translucent')
          chartRef.current.options.scales.x.ticks.color = getStyle('--cui-body-color')
          chartRef.current.options.scales.y.grid.borderColor = getStyle(
            '--cui-border-color-translucent',
          )
          chartRef.current.options.scales.y.grid.color = getStyle('--cui-border-color-translucent')
          chartRef.current.options.scales.y.ticks.color = getStyle('--cui-body-color')
          chartRef.current.update()
        })
      }
    })
  }, [chartRef])

  const random = () => Math.round(Math.random() * 100)

  return (
    <>
      <CChartLine
        ref={chartRef}
        style={{ height: '300px', marginTop: '40px' }}
        data={{
          labels: labels,
          datasets: [
            {
              label: 'Accepted',
              backgroundColor: `rgba(${getStyle('--cui-success-rgb')}, .1)`,
              borderColor: getStyle('--cui-success'),
              pointHoverBackgroundColor: getStyle('--cui-success'),
              borderWidth: 2,
              data: acceptedCandidatesMonthlyData,
              fill: true,
            },
            {
              label: 'Rejected',
              backgroundColor: 'transparent',
              borderColor: getStyle('--cui-danger'),
              pointHoverBackgroundColor: getStyle('--cui-danger'),
              borderWidth: 2,
              data: rejectedCandidatesMonthlyData,
            },
            {
              label: 'Active applications',
              backgroundColor: 'transparent',
              borderColor: getStyle('--cui-info'),
              pointHoverBackgroundColor: getStyle('--cui-info'),
              borderWidth: 2,
              data: pendingCandidatesMonthlyData,
            },
          ],
        }}
        options={{
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: false,
            },
          },
          scales: {
            x: {
              grid: {
                color: getStyle('--cui-border-color-translucent'),
                drawOnChartArea: false,
              },
              ticks: {
                color: getStyle('--cui-body-color'),
              },
            },
            y: {
              beginAtZero: true,
              border: {
                color: getStyle('--cui-border-color-translucent'),
              },
              grid: {
                color: getStyle('--cui-border-color-translucent'),
              },
              max: 250,
              ticks: {
                color: getStyle('--cui-body-color'),
                maxTicksLimit: 5,
                stepSize: Math.ceil(250 / 5),
              },
            },
          },
          elements: {
            line: {
              tension: 0.4,
            },
            point: {
              radius: 0,
              hitRadius: 10,
              hoverRadius: 4,
              hoverBorderWidth: 3,
            },
          },
        }}
      />
    </>
  )
}

export default MainChart
