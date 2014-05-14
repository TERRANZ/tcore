package ru.terra.server.controller;

import com.sun.jersey.api.core.HttpContext;
import flexjson.JSONDeserializer;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import ru.terra.server.constants.CoreUrlConstants;
import ru.terra.server.constants.ErrorConstants;
import ru.terra.server.dto.CommonDTO;
import ru.terra.server.dto.ListDTO;
import ru.terra.server.dto.SimpleDataDTO;
import ru.terra.server.engine.AbstractEngine;
import ru.terra.server.security.SecurityLevel;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;

public abstract class AbstractController<Bean, ReturnDto extends CommonDTO, Engine extends AbstractEngine<Bean, ReturnDto>> extends AbstractResource {

    private Boolean checkUserAccess;
    private Class<Bean> beanClass;
    private Class<ReturnDto> dtoClass;
    protected Engine engine;

    private Logger logger = Logger.getLogger(AbstractController.class);

    public AbstractController(Class<Engine> engineClass, Boolean checkUserAccess, Class<Bean> beanClass, Class<ReturnDto> dtoClass) {
        this.checkUserAccess = checkUserAccess;
        this.beanClass = beanClass;
        this.dtoClass = dtoClass;
        try {
            engine = engineClass.newInstance();
        } catch (InstantiationException e) {
            logger.error("Unable to instantiate egine", e);
        } catch (IllegalAccessException e) {
            logger.error("Unable to access to engine class", e);
        }
    }

    @GET
    @Path(CoreUrlConstants.DoJson.DO_LIST)
    public ListDTO<ReturnDto> list(@Context HttpContext hc, @QueryParam("all") Boolean all, @QueryParam("page") Integer page, @QueryParam("perpage") Integer perpage) {
        if (engine == null)
            throw new NotImplementedException();
        if (checkUserAccess && !isAuthorized(hc)) {
            ListDTO<ReturnDto> ret = new ListDTO<>();
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return ret;
        }
        ListDTO<ReturnDto> ret = new ListDTO<>();
        if (all == null)
            all = true;
        if (page == null)
            page = -1;
        if (perpage == null)
            perpage = -1;
        ret.setData(engine.listDtos(all, page, perpage));
        return ret;
    }

    @GET
    @Path(CoreUrlConstants.DoJson.DO_GET)
    public ReturnDto get(@Context HttpContext hc, @QueryParam("id") Integer id) {
        if (engine == null)
            throw new NotImplementedException();
        if (checkUserAccess && !isAuthorized(hc)) {
            CommonDTO ret = new CommonDTO();
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return (ReturnDto) ret;
        }
        return engine.getDto(id);
    }

    @DELETE
    @Path(CoreUrlConstants.DoJson.DO_DEL)
    public SimpleDataDTO<Boolean> delete(@Context HttpContext hc, @QueryParam("id") Integer id) {
        if (engine == null)
            throw new NotImplementedException();
        if (!checkUserCanAccess(hc, SecurityLevel.MANAGER)) {
            SimpleDataDTO<Boolean> ret = new SimpleDataDTO<Boolean>(false);
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return ret;
        }
        return new SimpleDataDTO<>(engine.delete(id));
    }

    @PUT
    @Path(CoreUrlConstants.DoJson.DO_CREATE)
    public ReturnDto create(@Context HttpContext hc, @QueryParam("json") String json) {
        if (engine == null)
            throw new NotImplementedException();
        if (checkUserAccess && !isAuthorized(hc)) {
            CommonDTO ret = new CommonDTO();
            ret.errorCode = ErrorConstants.ERR_NOT_AUTHORIZED_ID;
            ret.errorMessage = ErrorConstants.ERR_NOT_AUTHORIZED_MSG;
            return (ReturnDto) ret;
        }
        ReturnDto newDto = new JSONDeserializer<ReturnDto>().deserialize(json, dtoClass);
        Bean bean = null;
        try {
            bean = beanClass.newInstance();
        } catch (InstantiationException e) {
            logger.error("Unable to instantiate bean class", e);
        } catch (IllegalAccessException e) {
            logger.error("Illeagal access exception", e);
        }
        engine.dtoToEntity(newDto, bean);
        return engine.entityToDto(engine.createBean(bean));
    }
}
