import React from "react";
import Chart from "react-apexcharts";

export interface MixedChartProps {
  total: number[];
  interest: number[];
}

const MixedChart: React.FC<MixedChartProps> = ({ total, interest }) => {
  const options = {
    series: [
      {
        name: "유동인구수(명)",
        type: "column",
        data: total,
      },
      {
        name: "관심도(%)",
        type: "line",
        data: interest,
      },
    ],
    xaxis: {
      categories: [6, 7, 8, 9, 10, 11, 12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11],
    },
    height: 300,
  };

  return <Chart options={options} series={options.series} />;
};

export default MixedChart;
