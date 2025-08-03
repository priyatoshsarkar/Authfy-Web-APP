import React, { useRef, useState, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { toast } from 'react-toastify';
import { assets } from '../assets/assets';
import { AppContext } from '../context/AppContext';

const ResetPassword = () => {
  const inputRef = useRef([]);
  const navigate = useNavigate();
  const { backendURL } = useContext(AppContext);

  const [email, setEmail] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [otp, setOtp] = useState('');
  const [loading, setLoading] = useState(false);
  const [isEmailSent, setIsEmailSent] = useState(false);
  const [isOtpSubmitted, setIsOtpSubmitted] = useState(false);

  const handleOTPChange = (e, index) => {
    const value = e.target.value.replace(/\D/, '');
    e.target.value = value;
    if (value && index < 5) inputRef.current[index + 1]?.focus();
  };

  const handleKeyDown = (e, index) => {
    if (e.key === 'Backspace' && !e.target.value && index > 0) {
      inputRef.current[index - 1]?.focus();
    }
  };

  const handlePaste = (e) => {
    e.preventDefault();
    const pasted = e.clipboardData.getData('text').slice(0, 6).split('');
    pasted.forEach((digit, i) => {
      if (inputRef.current[i]) {
        inputRef.current[i].value = digit;
      }
    });
    const nextIndex = Math.min(pasted.length, 5);
    inputRef.current[nextIndex]?.focus();
  };

  const handleEmailSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      //const response = await axios.get(backendURL+"/send-reset-otp",{ email },{ withCredentials: true });
      const response = await axios.post(`${backendURL}/send-reset-otp?email=${encodeURIComponent(email)}`, {withCredentials: true});
      if (response.status === 200) {
        toast.success('OTP sent to your email');
        setIsEmailSent(true);
      }
    } catch (error) {
      toast.error(error?.response?.data?.message || error.message || 'Failed to send OTP');
    } finally {
      setLoading(false);
    }
  };

  const handleOTPVerify = () => {
    const enteredOtp = inputRef.current.map((input) => input.value).join('');
    if (enteredOtp.length !== 6) {
      toast.error('Please enter all 6 digits of the OTP');
      return;
    }
    setOtp(enteredOtp);
    setIsOtpSubmitted(true);
  };

  const handlePasswordSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      //const response = await axios.post(`${backendURL}/reset-password?otp=${otp}`,{ email, newPassword },{ withCredentials: true });
     const response = await axios.post(
      `${backendURL}/reset-password`,
      {
        email,
        otp,           
        newPassword
      },
      {
        withCredentials: true,
        headers: {
          'Content-Type': 'application/json',
        },
      }
    );
      if (response.status === 200) {
        toast.success('Password reset successful');
        navigate('/login');
      }
    } catch (error) {
      toast.error(error?.response?.data?.message || error.message || 'Failed to reset password');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="email-verify-container d-flex align-items-center justify-content-center vh-100 position-relative"
      style={{ background: "linear-gradient(90deg, #6a5af9, #8268f9)" }}>

      <Link to="/login" className="position-absolute top-0 start-0 p-4 d-flex align-items-center gap-2 text-decoration-none">
        <img src={assets.logo} alt="logo" width={32} height={32} />
        <span className="fs-4 fw-semibold text-light">Authify</span>
      </Link>

      {!isEmailSent && (
        <div className="rounded-4 p-5 text-center bg-white" style={{ width: "100%", maxWidth: "400px" }}>
          <h4 className="mb-2">Reset Password</h4>
          <p className="mb-4">Enter your registered email</p>
          <form onSubmit={handleEmailSubmit}>
            <div className="input-group mb-4 bg-secondary bg-opacity-10 rounded-pill">
              <span className="input-group-text bg-transparent border-0 ps-4">
                <i className="bi bi-envelope"></i>
              </span>
              <input
                type="email"
                className="form-control bg-transparent border-0 ps-1 pe-4 rounded-end"
                placeholder="Enter your email address"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
            <button className="btn btn-primary w-100 py-2" type="submit" disabled={loading}>
              {loading ? 'Sending...' : 'Submit'}
            </button>
          </form>
        </div>
      )}

      {isEmailSent && !isOtpSubmitted && (
        <div className="p-5 rounded-4 shadow bg-white" style={{ width: "400px" }}>
          <h4 className="text-center fw-bold mb-2">Email Verification</h4>
          <p className="text-center mb-4">Enter the 6-digit OTP sent to your email.</p>
          <div className="d-flex justify-content-between gap-2 mb-4">
            {[...Array(6)].map((_, i) => (
              <input
                key={i}
                type="text"
                maxLength={1}
                className="form-control text-center fs-4 otp-input"
                ref={(el) => (inputRef.current[i] = el)}
                onChange={(e) => handleOTPChange(e, i)}
                onKeyDown={(e) => handleKeyDown(e, i)}
                onPaste={handlePaste}
              />
            ))}
          </div>
          <button className="btn btn-primary w-100 fw-semibold" onClick={handleOTPVerify} disabled={loading}>
            {loading ? 'Verifying...' : 'Verify OTP'}
          </button>
        </div>
      )}

      {isEmailSent && isOtpSubmitted && (
        <div className="rounded-4 p-4 text-center bg-white" style={{ width: "100%", maxWidth: "400px" }}>
          <h4>Set New Password</h4>
          <p className="mb-4">Enter your new password below</p>
          <form onSubmit={handlePasswordSubmit}>
            <div className="input-group mb-4 bg-secondary bg-opacity-10 rounded-pill">
              <span className="input-group-text bg-transparent border-0 ps-4">
                <i className="bi bi-person-fill-lock"></i>
              </span>
              <input
                type="password"
                className="form-control bg-transparent border-0 ps-1 pe-4 rounded-end"
                placeholder="**************"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                required
              />
            </div>
            <button className="btn btn-primary w-100" type="submit" disabled={loading}>
              {loading ? 'Resetting...' : 'Reset Password'}
            </button>
          </form>
        </div>
      )}
    </div>
  );
};

export default ResetPassword;
