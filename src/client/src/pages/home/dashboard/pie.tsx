import React from "react";
import Chart from "react-apexcharts";

export const InterestPeopleChart = ({ interestPeopleCount }: any) => {
  const names = ["남성", "여성"];

  return React.createElement(Chart, {
    type: "pie",
    series: interestPeopleCount,
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
      colors: ["#008FFB", "#FF4560"],
    },
  });
};
