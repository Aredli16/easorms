'use client';

import { signOut } from 'next-auth/react';

const Page = () => {
  return (
    <div>
      <h1>Register Page</h1>
      <button onClick={() => signOut()}>Logout</button>
    </div>
  );
};

export default Page;
