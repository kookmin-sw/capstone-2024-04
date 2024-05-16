import React from "react";
import Chart from "react-apexcharts";

export const TargetInterestChart = () => {
  const names = ["관심을 지닌 타겟층의 수", "타겟층 중 지나간 사람"];

  return React.createElement(Chart, {
    type: "donut",
    series: [30, 42],
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
        position: "left",
      },
      colors: ["#4047F4", "#F0F0F0"],
    },
  });
};
