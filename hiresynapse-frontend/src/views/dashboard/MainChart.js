import React, {useEffect, useRef} from 'react'

import {CChartLine} from '@coreui/react-chartjs'
import {getStyle} from '@coreui/utils'

const MainChart = ({ stats }) => {
  const chartRef = useRef(null)

  const acceptedCandidatesMonthlyData = Object.values(stats.acceptedCandidatesFromLastSixMonths.monthlyData)
  const rejectedCandidatesMonthlyData = Object.values(stats.rejectedCandidatesFromLastSixMonths.monthlyData)
  const pendingCandidatesMonthlyData = Object.values(stats.pendingCandidatesFromLastSixMonths.monthlyData)
  const scheduledInterviewsMonthlyData = Object.values(stats.scheduledInterviewsFromLastSixMonths.monthlyData)
  const completedInterviewsMonthlyData = Object.values(stats.completedInterviewsFromLastSixMonths.monthlyData)

  const labels = Object.keys(stats.acceptedCandidatesFromLastSixMonths.monthlyData)

  const allData = [
    ...acceptedCandidatesMonthlyData,
    ...rejectedCandidatesMonthlyData,
    ...pendingCandidatesMonthlyData,
    ...scheduledInterviewsMonthlyData,
    ...completedInterviewsMonthlyData,
  ];
  const maxValue = Math.max(...allData);
  const stepSize = Math.ceil(maxValue / 5) || 1;

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

  return (
    <>
      <CChartLine
        ref={chartRef}
        style={{ height: '300px', marginTop: '40px' }}
        data={{
          labels: labels,
          datasets: [
            {
              label: 'Accepted candidates',
              backgroundColor: `rgba(${getStyle('--cui-success-rgb')}, .1)`,
              borderColor: getStyle('--cui-success'),
              pointHoverBackgroundColor: getStyle('--cui-success'),
              borderWidth: 2,
              data: acceptedCandidatesMonthlyData,
              fill: true,
            },
            {
              label: 'Rejected candidates',
              backgroundColor: 'transparent',
              borderColor: getStyle('--cui-danger'),
              pointHoverBackgroundColor: getStyle('--cui-danger'),
              borderWidth: 2,
              data: rejectedCandidatesMonthlyData,
            },
            {
              label: 'Pending applications',
              backgroundColor: 'transparent',
              borderColor: getStyle('--cui-info'),
              pointHoverBackgroundColor: getStyle('--cui-info'),
              borderWidth: 2,
              data: pendingCandidatesMonthlyData,
            },
            {
              label: 'Scheduled interviews',
              backgroundColor: 'transparent',
              borderColor: getStyle('--cui-warning'),
              pointHoverBackgroundColor: getStyle('--cui-warning'),
              borderWidth: 2,
              data: scheduledInterviewsMonthlyData,
            },
            {
              label: 'Completed interviews',
              backgroundColor: 'transparent',
              borderColor: getStyle('--cui-primary'),
              pointHoverBackgroundColor: getStyle('--cui-primary'),
              borderWidth: 2,
              data: completedInterviewsMonthlyData,
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
              max: maxValue + stepSize,
              border: {
                color: getStyle('--cui-border-color-translucent'),
              },
              grid: {
                color: getStyle('--cui-border-color-translucent'),
              },
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
