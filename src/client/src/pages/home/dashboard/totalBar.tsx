import React from "react";
import Chart from "react-apexcharts";

export const TotalBar = ({ totalAge }: any) => {
  const names = [""];

  return React.createElement(Chart, {
    options: {
      chart: {
        type: "bar",
        stacked: true,
        stackType: "100%",
      },
      plotOptions: {
        bar: {
          horizontal: true,
        },
      },
      xaxis: {
        categories: names,
        labels: {
          show: false,
        },
      },
      legend: {
        show: false,
      },
      colors: [
        "#ff6929",
        "#FFB800",
        "#75EB2D",
        "#1FD7FF",
        "#7941F1",
        "#999999",
      ],
    },
    series: totalAge,
    type: "bar",
    height: 120,
  });
};
