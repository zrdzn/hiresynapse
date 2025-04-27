import {CButton} from "@coreui/react";
import {useState} from "react";

export const SelectCalendar = (props) => {
  const [currentMonth, setCurrentMonth] = useState(new Date())

  const getDaysInMonth = (year, month) => {
    return new Date(year, month + 1, 0).getDate()
  }

  const getFirstDayOfMonth = (year, month) => {
    return new Date(year, month, 1).getDay()
  }

  const renderCalendar = () => {
    const year = currentMonth.getFullYear()
    const month = currentMonth.getMonth()

    const daysInMonth = getDaysInMonth(year, month)
    const firstDayOfMonth = getFirstDayOfMonth(year, month)

    const days = []

    for (let i = 0; i < firstDayOfMonth; i++) {
      days.push(
        <div
          key={`empty-${i}`}
          className="bg-body-secondary border-end border-bottom"
          style={{ width: "14.28%", height: "40px" }}
        ></div>,
      )
    }

    for (let day = 1; day <= daysInMonth; day++) {
      const date = new Date(year, month, day)
      const isSelected =
        props.selectedDate &&
        date.getDate() === props.selectedDate.getDate() &&
        date.getMonth() === props.selectedDate.getMonth() &&
        date.getFullYear() === props.selectedDate.getFullYear()

      days.push(
        <div
          key={`day-${day}`}
          className={`d-flex align-items-center justify-content-center border-end border-bottom ${
            isSelected ? "bg-primary text-white" : ""
          }`}
          style={{
            width: "14.28%",
            height: "40px",
            cursor: "pointer",
          }}
          onClick={() => props.onSelectDate(date)}
        >
          {day}
        </div>,
      )
    }

    return days
  }

  const prevMonth = () => {
    setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() - 1, 1))
  }

  const nextMonth = () => {
    setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() + 1, 1))
  }

  const monthNames = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ]

  return (
    <div className="calendar">
      <div className="d-flex justify-content-between align-items-center mb-2">
        <CButton className="bg-body-secondary" onClick={prevMonth}>
          &lt;
        </CButton>
        <h5 className="mb-0">
          {monthNames[currentMonth.getMonth()]} {currentMonth.getFullYear()}
        </h5>
        <CButton className="bg-body-secondary" onClick={nextMonth}>
          &gt;
        </CButton>
      </div>

      <div className="border rounded overflow-hidden">
        <div className="d-flex bg-body-secondary border-bottom">
          {["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"].map((day) => (
            <div key={day} className="fw-bold text-center py-2 text-body" style={{width: "14.28%"}}>
              {day}
            </div>
          ))}
        </div>

        <div className="d-flex flex-wrap" style={{minHeight: "240px"}}>
          {renderCalendar()}
        </div>
      </div>
    </div>
  )
}
