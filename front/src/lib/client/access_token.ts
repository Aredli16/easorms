import { getSession } from 'next-auth/react';

export const getAccessToken = async () => {
  const session = await getSession();

  if (!session)
    throw 'No session';

  return session.access_token;
};

