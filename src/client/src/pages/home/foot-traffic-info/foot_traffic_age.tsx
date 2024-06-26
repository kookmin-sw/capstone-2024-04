import React from "react";
import Chart from "react-apexcharts";

export const FootTrafficAgeChart = () => {
  const names = ["10대", "20대", "30대", "40대", "50대", "60대 이상"];

  return React.createElement(Chart, {
    type: "donut",
    series: [566818, 476025, 273018, 344015, 294344, 209961],
    height: "100%",
    labels: {
      show: false,
      name: {
        show: true,
      },
    },
    options: {
      labels: names,
      legend: {
        show: true,
        position: "right",
      },
    },
  });
};
