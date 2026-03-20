export const formatModel = (model: string) => {
  switch (model) {
    case "FULL_TIME":
      return "Vollzeit";
    case "PART_TIME":
      return "Berufsbegleitend";
    case "HYBRID":
      return "Hybrid / flexibel";
    default:
      return model;
  }
};

export const titleizeTag = (tag: string) =>
  tag.replace(/_/g, " ").replace(/\b\w/g, (char) => char.toUpperCase());
