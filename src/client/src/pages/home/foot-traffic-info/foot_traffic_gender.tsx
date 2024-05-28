import React from "react";
import Chart from "react-apexcharts";

export const FootTrafficGenderChart = () => {
  const names = ["남성", "여성"];

  return React.createElement(Chart, {
    type: "pie",
    series: [912038, 1252800],
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
      colors: ["#008FFB", "#FF4560"],
    },
  });
};
