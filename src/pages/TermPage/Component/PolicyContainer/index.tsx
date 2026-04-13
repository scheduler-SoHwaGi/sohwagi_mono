interface PolicyContainerProps {
  children: React.ReactNode;
}

function PolicyContainer({ children }: PolicyContainerProps) {
  return <div>{children}</div>;
}

export default PolicyContainer;
