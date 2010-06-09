package com.jamesward.census2.server;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jamesward.census2.client.CensusGWTService;
import com.jamesward.census2.CensusDAO;
import com.jamesward.census2.shared.CensusEntryVO;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CensusGWTServiceImpl extends RemoteServiceServlet implements CensusGWTService
{

	@Override
	public CensusEntryVO add(CensusEntryVO data)
	{
		
		return null;
	}

	@Override
	public List<CensusEntryVO> fetch(Integer startRow, Integer endRow, String sortBy)
	{
		ArrayList<CensusEntryVO> entries = new ArrayList<CensusEntryVO>();
		
		CensusDAO dao = new CensusDAO();
        try
        {
            CensusEntryVO[] list = dao.getList(startRow, endRow);
            for (CensusEntryVO entry : list)
            {
            	entries.add(entry);
            }
            
            return entries;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
	}

	@Override
	public void remove(CensusEntryVO data)
	{

	}

	@Override
	public CensusEntryVO update(CensusEntryVO data)
	{
		return null;
	}
	
}