import { useEffect, useState } from "react"
import { AddInterface } from "../module/addmodule";
import { ApiService } from "./api-service/apiservice";
import { useNavigate } from "react-router-dom";

const AddService = () => {

  const [categoryid, setCategoryid] = useState<string>('');
  const [amount, setAmount] = useState<number | "">();
  const [name, setName] = useState<string>('');
  const [description, setDescription] = useState<string>('');
  const [type, setType] = useState<string>('');
  const [date, setDate] = useState<string>('');
  const [error, setError] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const apiService = new ApiService();
  const navigate = useNavigate();
  const userId = localStorage.getItem("userId");
  const token = localStorage.getItem("token");

  useEffect(() => {

    if (!token) {
      navigate("/login");
    }
  }, [navigate]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {

    const { name, value } = e.target;
    if (name === 'categoryid') {
      if (value === "") {
        if (name === "categoryid") {
          setCategoryid(value);
          setError("");
        }
        return;
      }

      const regex = /^[1-9]\d*$/;

      if (name === "categoryid") {
        if (!regex.test(value)) {
          setError("CategoryId should be a positive number");
          return;
        }
        setCategoryid(value);
        setError("");
      }
    }
    else if (name === 'amount') {
      if (value === "") {
        setAmount("");
        setError("");
      }
      else {
        const num = Number(value);
        if (num > 0 && Number.isInteger(num)) {
          setAmount(num);
          setError("");
        }
        else {
          setError("Amount Should be a positive number");
        }
      }
    }
    else if (name === 'name') {
      setName(value);
      if (/\d/.test(value)) {
        setError('Input cannot contain numbers.');
      } else {
        setError('');
      }
    }
    else if (name === 'description') {
      setDescription(value);
      if (/\d/.test(value)) {
        setError('Input cannot contain numbers.');
      } else {
        setError('');
      }
    }
    else if (name === 'type') {
      setType(value);
      if (/\d/.test(value)) {
        setError('Input cannot contain numbers.');
      } else {
        setError('');
      }
    }
    else if (name === 'date') {
      setDate(value);
    }
  };

  const handleSubmit = async () => {

    if (!categoryid || !amount || !name || !description || !type || !date) {
      setError("Please Fill All Blanks Correctly");
      return;
    }

    if (amount < 0) {
      setError("Amount Should Be Positive Value");
      return;
    }

    if (userId && token) {

      setLoading(true);

      try {

        const params: AddInterface.param = { userId: userId.trim(), categoryid: String(categoryid.trim()), amount: amount, name: name.trim(), description: description.trim(), type: type.trim(), date: date };
        await apiService.sendRequest(AddInterface.name, params, navigate);

        navigate("/homepage");

      }

      catch (error) {

        setError(error instanceof Error ? error.message : "Something went wrong");
      }
    }

    setLoading(false);
  }

  return {
    categoryid,
    amount,
    name,
    description,
    type,
    date,
    error,
    loading,
    handleInputChange,
    handleSubmit
  }

}

export default AddService;
