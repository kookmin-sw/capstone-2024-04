interface TextProps {
  text: string;
  color: string;
}

/** 18px, Regular */
const Headline1 = ({ text, color }: TextProps) => {
  return <h2 className={`text-lg font-normal ${color}`}>{text}</h2>;
};

/** 18px, Medium */
const Headline2 = ({ text, color }: TextProps) => {
  return <h2 className={`text-lg font-medium ${color}`}>{text}</h2>;
};

/** 18px, SemiBold */
const Headline3 = ({ text, color }: TextProps) => {
  return <h2 className={`text-lg font-semibold ${color}`}>{text}</h2>;
};

/** 16px, Regular */
const Subtitle1 = ({ text, color }: TextProps) => {
  return <h3 className={`text-base font-normal ${color}`}>{text}</h3>;
};

/** 16px, Medium */
const Subtitle2 = ({ text, color }: TextProps) => {
  return <h3 className={`text-base font-medium ${color}`}>{text}</h3>;
};

/** 16px, SemiBold */
const Subtitle3 = ({ text, color }: TextProps) => {
  return <h3 className={`text-base font-semibold ${color}`}>{text}</h3>;
};

/** 14px, Regular */
const Body1 = ({ text, color }: TextProps) => {
  return <p className={`text-sm font-normal ${color}`}>{text}</p>;
};

/** 14px, Medium */
const Body2 = ({ text, color }: TextProps) => {
  return <p className={`text-sm font-medium ${color}`}>{text}</p>;
};

/** 12px, Regular */
const Caption1 = ({ text, color }: TextProps) => {
  return <p className={`text-xs font-normal ${color}`}>{text}</p>;
};

/** 12px, Medium */
const Caption2 = ({ text, color }: TextProps) => {
  return <p className={`text-xs font-medium ${color}`}>{text}</p>;
};

export {
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
