interface TextProps {
  text: string;
  color: null | string;
}

const Headline1 = ({ text, color }: TextProps) => {
  return (
    <h2
      className={`text-lg font-normal ${
        color !== null ? `text-${color}` : "text-black"
      }`}
    >
      {text}
    </h2>
  );
};

const Headline2 = ({ text, color }: TextProps) => {
  return (
    <h2
      className={`text-lg font-medium ${
        color !== null ? `text-${color}` : "text-black"
      }`}
    >
      {text}
    </h2>
  );
};

const Headline3 = ({ text, color }: TextProps) => {
  return (
    <h2
      className={`text-lg font-semibold ${
        color !== null ? `text-${color}` : "text-black"
      }`}
    >
      {text}
    </h2>
  );
};

const Subtitle1 = ({ text, color }: TextProps) => {
  return (
    <h3
      className={`text-base font-normal ${
        color !== null ? `text-${color}` : "text-black"
      }`}
    >
      {text}
    </h3>
  );
};

const Subtitle2 = ({ text, color }: TextProps) => {
  return (
    <h3
      className={`text-base font-medium ${
        color !== null ? `text-${color}` : "text-black"
      }`}
    >
      {text}
    </h3>
  );
};

const Subtitle3 = ({ text, color }: TextProps) => {
  return (
    <h3
      className={`text-base font-semibold ${
        color !== null ? `text-${color}` : "text-black"
      }`}
    >
      {text}
    </h3>
  );
};

const Body1 = ({ text, color }: TextProps) => {
  return (
    <p
      className={`text-sm font-semibold ${
        color !== null ? `text-${color}` : "text-black"
      }`}
    >
      {text}
    </p>
  );
};

const Body2 = ({ text, color }: TextProps) => {
  return (
    <p
      className={`text-sm font-semibold ${
        color !== null ? `text-${color}` : "text-black"
      }`}
    >
      {text}
    </p>
  );
};

const Caption1 = ({ text, color }: TextProps) => {
  return (
    <p
      className={`text-xs font-semibold ${
        color !== null ? `text-${color}` : "text-black"
      }`}
    >
      {text}
    </p>
  );
};

const Caption2 = ({ text, color }: TextProps) => {
  return (
    <p
      className={`text-xs font-semibold ${
        color !== null ? `text-${color}` : "text-black"
      }`}
    >
      {text}
    </p>
  );
};

export default {
  Headline1,
  Headline2,
  Headline3,
  Subtitle1,
  Subtitle2,
  Subtitle3,
  Body1,
  Body2,
  Caption1,
  Caption2,
};
