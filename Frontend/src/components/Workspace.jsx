import { useParams } from "react-router-dom"


export default function Workspace() {
  const URL = useParams();

  return <div>{URL.id}</div>
}
